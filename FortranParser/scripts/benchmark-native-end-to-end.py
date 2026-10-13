#!/usr/bin/env python3
"""Run parity-gated, isolated-JVM JSON/protobuf parser measurements."""

from __future__ import annotations

import argparse
import hashlib
import json
import os
import platform
import shutil
import statistics
import subprocess
import sys
import tempfile
from datetime import datetime, timezone
from pathlib import Path
from typing import Any


PARSER_DIR = Path(__file__).resolve().parents[1]
REPO_ROOT = PARSER_DIR.parent
MAIN_CLASS = "pt.up.fe.specs.fortran.parser.FortranEndToEndBenchmark"
JAVA_MAIN = PARSER_DIR / "src/pt/up/fe/specs/fortran/parser/FortranEndToEndBenchmark.java"
SCHEMA = PARSER_DIR / "src/main/proto/ast_stream.proto"
FIXTURE_ROOT = PARSER_DIR / "resources-test"
GNU_TIME = Path("/usr/bin/time")
SMALL_SOURCE = FIXTURE_ROOT / "fortran/parser/hello.f90"
LARGE_SOURCE = FIXTURE_ROOT / "fortran/parser/polybench/3mm.f90"
HEAP_ARGS = ["-Xms256m", "-Xmx1g"]


def sha256_file(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def synthetic_source(statement_count: int) -> bytes:
    lines = [
        "program synthetic_protocol_benchmark",
        "  implicit none",
        "  integer :: value",
        "  value = 0",
    ]
    lines.extend(f"  value = value + {index % 97 + 1}" for index in range(statement_count))
    lines.append("end program synthetic_protocol_benchmark")
    return ("\n".join(lines) + "\n").encode("ascii")


def executable(path: str, name: str) -> Path:
    resolved = Path(path).expanduser().resolve()
    if not resolved.is_file() or not os.access(resolved, os.X_OK):
        raise RuntimeError(f"{name} must be an executable file: {resolved}")
    return resolved


def git_output(*args: str) -> str:
    result = subprocess.run(
        ["git", *args], cwd=REPO_ROOT, capture_output=True, text=True, check=False
    )
    return result.stdout.strip() if result.returncode == 0 else ""


def gradle_command() -> list[str]:
    candidates = [REPO_ROOT / "gradlew", PARSER_DIR / "gradlew"]
    for candidate in candidates:
        if candidate.is_file() and os.access(candidate, os.X_OK):
            return [str(candidate.resolve())]
    gradle = shutil.which("gradle")
    if gradle:
        return [gradle]
    raise RuntimeError("Gradle is unavailable (no executable wrapper or gradle on PATH)")


def ensure_classpath(env: dict[str, str]) -> tuple[str, list[str], str]:
    command = gradle_command() + [
        "--no-daemon",
        "-p",
        str(PARSER_DIR),
        "writeEndToEndBenchmarkClasspath",
    ]
    result = subprocess.run(command, cwd=REPO_ROOT, env=env, capture_output=True, text=True)
    classpath_file = PARSER_DIR / "build/benchmark/end-to-end-classpath.txt"
    if result.returncode != 0:
        detail = (result.stdout + "\n" + result.stderr).strip()
        raise RuntimeError(f"Gradle classpath task failed ({result.returncode}):\n{detail[-8000:]}")
    if not classpath_file.is_file():
        raise RuntimeError(f"Gradle did not create the classpath file: {classpath_file}")
    classpath = classpath_file.read_text(encoding="utf-8").strip()
    if not classpath:
        raise RuntimeError(f"Gradle wrote an empty classpath file: {classpath_file}")
    return classpath, command, result.stdout.strip()


def make_flang_wrapper(directory: Path) -> Path:
    wrapper = directory / "flang-with-rss"
    wrapper.write_text(
        "#!/bin/sh\n"
        "set -eu\n"
        ": \"${FLANG_REAL_BIN:?FLANG_REAL_BIN is required}\"\n"
        ": \"${FLANG_RSS_FILE:?FLANG_RSS_FILE is required}\"\n"
        "exec /usr/bin/time -f '%e\\t%M' -a -o \"$FLANG_RSS_FILE\" \\\n"
        "  \"$FLANG_REAL_BIN\" \"$@\"\n",
        encoding="utf-8",
    )
    wrapper.chmod(0o755)
    return wrapper


def parse_time_file(path: Path, label: str) -> list[dict[str, float | int]]:
    if not path.is_file():
        raise RuntimeError(f"Missing {label} GNU time output: {path}")
    records: list[dict[str, float | int]] = []
    for line in path.read_text(encoding="utf-8").splitlines():
        fields = line.split("\t")
        if len(fields) != 2:
            raise RuntimeError(f"Malformed {label} GNU time output: {line!r}")
        records.append(
            {
                "gnu_time_elapsed_seconds": float(fields[0]),
                "gnu_time_wait_rusage_maxrss_kib": int(fields[1]),
            }
        )
    return records


def invoke_java(
    *,
    env: dict[str, str],
    classpath: str,
    wrapper: Path,
    temp_dir: Path,
    source: Path,
    mode: str,
    plugin: Path,
    warmups: int,
    timed: bool,
) -> dict[str, Any]:
    invocation_id = f"{mode}-{source.stem}-{len(list(temp_dir.glob('child-*.txt')))}"
    child_time_file = temp_dir / f"child-{invocation_id}.txt"
    host_time_file = temp_dir / f"host-{invocation_id}.txt"
    child_env = dict(env)
    child_env["FLANG_REAL_BIN"] = str(child_env["BENCH_FLANG_REAL_BIN"])
    child_env["FLANG_RSS_FILE"] = str(child_time_file)

    java_command = [
        "java",
        *HEAP_ARGS,
        "-cp",
        classpath,
        MAIN_CLASS,
        f"--mode={mode}",
        f"--source={source}",
        f"--plugin={plugin}",
        f"--flang={wrapper}",
        f"--warmups={warmups}",
    ]
    command = (
        [str(GNU_TIME), "-f", "%e\t%M", "-o", str(host_time_file), *java_command]
        if timed
        else java_command
    )
    result = subprocess.run(command, cwd=PARSER_DIR, env=child_env, capture_output=True, text=True)
    if result.returncode != 0:
        raise RuntimeError(
            f"Java {mode} run failed ({result.returncode}) for {source}:\n"
            f"stdout:\n{result.stdout[-5000:]}\nstderr:\n{result.stderr[-5000:]}"
        )

    records = []
    process_records = []
    for line in result.stdout.splitlines():
        if line.startswith("BENCH_RESULT\t"):
            records.append(json.loads(line.partition("\t")[2]))
        elif line.startswith("BENCH_PROCESS\t"):
            process_records.append(json.loads(line.partition("\t")[2]))
    expected = warmups + 1
    if len(records) != expected:
        raise RuntimeError(f"Expected {expected} BENCH_RESULT records, got {len(records)}")
    if len(process_records) != 1:
        raise RuntimeError(f"Expected one BENCH_PROCESS record, got {len(process_records)}")

    process_peak = process_records[0]
    process_peak_status = process_peak.get("status")
    process_peak_kib = process_peak.get("java_process_vm_hwm_kib")
    if process_peak_status not in ("supported", "unsupported"):
        raise RuntimeError(f"Invalid Java VmHWM status: {process_peak_status!r}")
    if process_peak_status == "supported" and (
        not isinstance(process_peak_kib, int) or isinstance(process_peak_kib, bool) or process_peak_kib < 0
    ):
        raise RuntimeError(f"Invalid supported Java VmHWM value: {process_peak_kib!r}")
    if process_peak_status == "unsupported" and process_peak_kib is not None:
        raise RuntimeError("Unsupported Java VmHWM measurement must be null")

    child_times = parse_time_file(child_time_file, "Flang child")
    if len(child_times) != expected:
        raise RuntimeError(f"Expected {expected} Flang child measurements, got {len(child_times)}")
    for record, child_time in zip(records, child_times, strict=True):
        record["producer_gnu_time_elapsed_seconds"] = child_time["gnu_time_elapsed_seconds"]
        record["producer_gnu_time_wait_rusage_maxrss_kib"] = child_time[
            "gnu_time_wait_rusage_maxrss_kib"
        ]

    host_time = None
    if timed:
        host_records = parse_time_file(host_time_file, "Java host")
        if len(host_records) != 1:
            raise RuntimeError(f"Expected one Java host measurement, got {len(host_records)}")
        host_time = host_records[0]

    return {
        "records": records,
        "java_command_gnu_time": host_time,
        "java_process_vm_hwm": process_peak,
        "stdout_tail": result.stdout[-3000:],
        "stderr_tail": result.stderr[-3000:],
    }


def source_metadata(source: Path, label: str) -> dict[str, Any]:
    return {
        "label": label,
        "path": str(source),
        "source_bytes": source.stat().st_size,
        "sha256": sha256_file(source),
    }


def summarize(measurements: list[dict[str, Any]], workload: str) -> dict[str, Any]:
    summaries: dict[str, Any] = {}
    for mode in ("json", "protobuf"):
        rows = [row for row in measurements if row["workload"] == workload and row["mode"] == mode]
        fields = (
            "parse_nanos",
            "ast_build_nanos",
            "get_code_nanos",
            "end_to_end_nanos",
            "producer_gnu_time_elapsed_seconds",
            "producer_gnu_time_wait_rusage_maxrss_kib",
            "java_command_gnu_time_elapsed_seconds",
            "java_command_gnu_time_wait_rusage_maxrss_kib",
            "java_process_vm_hwm_kib",
        )
        summaries[mode] = {}
        for field in fields:
            values = [row[field] for row in rows]
            if any(value is None for value in values) and not all(value is None for value in values):
                raise RuntimeError(f"Mixed supported and unsupported values for {field} in {mode} {workload}")
            summaries[mode][f"median_{field}"] = (
                None if all(value is None for value in values) else statistics.median(values)
            )
    comparisons = {}
    for field in (
        "end_to_end_nanos",
        "producer_gnu_time_elapsed_seconds",
        "producer_gnu_time_wait_rusage_maxrss_kib",
        "java_command_gnu_time_wait_rusage_maxrss_kib",
        "java_process_vm_hwm_kib",
    ):
        json_median = summaries["json"][f"median_{field}"]
        protobuf_median = summaries["protobuf"][f"median_{field}"]
        comparisons[f"protobuf_over_json_{field}_ratio"] = (
            protobuf_median / json_median if json_median not in (None, 0) and protobuf_median is not None else None
        )
        comparisons[f"protobuf_vs_json_{field}_percent"] = (
            (protobuf_median / json_median - 1) * 100
            if json_median not in (None, 0) and protobuf_median is not None
            else None
        )
    return {"workload": workload, "repeats": len([r for r in measurements if r["workload"] == workload and r["mode"] == "json"]), "modes": summaries, "comparison": comparisons}


def run(args: argparse.Namespace) -> dict[str, Any]:
    if not GNU_TIME.is_file():
        raise RuntimeError(f"GNU time is unavailable at {GNU_TIME}")
    if args.repeats < 3:
        raise RuntimeError("Use at least three independent JVM repeats")
    if args.warmups < 0:
        raise RuntimeError("--warmups must be at least zero")

    flang = executable(args.flang, "Flang")
    json_plugin = executable(args.json_plugin, "JSON plugin")
    protobuf_plugin = executable(args.protobuf_plugin, "protobuf plugin")
    env = os.environ.copy()
    specs_root = env.get("SPECS_JAVA_LIBS_HOME")
    if not specs_root or not Path(specs_root).is_dir():
        raise RuntimeError("Set SPECS_JAVA_LIBS_HOME to the local specs-java-libs checkout")
    env["SPECS_JAVA_LIBS_HOME"] = str(Path(specs_root).resolve())
    env["BENCH_FLANG_REAL_BIN"] = str(flang)
    classpath, build_command, gradle_output = ensure_classpath(env)

    synthetic_path = PARSER_DIR / "build/benchmark/synthetic-assignment-512.f90"
    synthetic_path.parent.mkdir(parents=True, exist_ok=True)
    synthetic_bytes = synthetic_source(512)
    synthetic_path.write_bytes(synthetic_bytes)
    workloads = [
        ("small", SMALL_SOURCE.resolve()),
        ("large", LARGE_SOURCE.resolve()),
        ("synthetic/assignment-chain-512", synthetic_path.resolve()),
    ]
    for label, source in workloads[:2]:
        if not source.is_file():
            raise RuntimeError(f"Required fixture is missing: {source}")

    git_status = git_output("status", "--short")
    report: dict[str, Any] = {
        "status": "running",
        "started_utc": datetime.now(timezone.utc).isoformat(),
        "configuration": {
            "repeats": args.repeats,
            "within_jvm_warmups_per_process": args.warmups,
            "heap_arguments": HEAP_ARGS,
            "measurement_order": "alternate JSON/protobuf first mode by repeat",
            "java_process_repetitions": "one fresh JVM for each mode/workload/repeat",
        },
        "measurement_method": {
            "parse_nanos": "FortranNativeParser.parse: starts before native process launch and ends after JSON/protobuf stream decoding and FortranJsonResult construction",
            "ast_build_nanos": "FortranAstBuilder.build",
            "get_code_nanos": "returned root FortranNode.getCode",
            "end_to_end_nanos": "direct nanoTime interval around parse, AST build, and getCode in the same JVM invocation",
            "java_process_vm_hwm_kib": "Java reads VmHWM from /proc/self/status after JVM startup, all in-process warmups, and the measured full-pipeline sample. This is the Java process's own high-water RSS and excludes child process RSS. It is null with an unsupported status when Linux /proc is unavailable.",
            "java_command_gnu_time_wait_rusage_maxrss_kib": "GNU time %M from wait3(2) resource usage for the directly timed Java command. On Linux, this wait usage can include the maximum RSS of a waited child, so it is not a Java-only peak and is not a summed process-tree peak.",
            "producer_gnu_time_wait_rusage_maxrss_kib": "GNU time %M from the wait resource usage for the Flang wrapper's command, recorded separately for each Flang invocation.",
            "gnu_time_evidence": {
                "local_documentation": "GNU time(1) says most reported information comes from wait3(2); Linux wait4(2) returns resource usage about the waited child; getrusage(2) documents RUSAGE_CHILDREN ru_maxrss as the largest child RSS, not a process-tree maximum.",
                "local_check": {
                    "timed_parent": "Python process waited for a Python child that allocated 67108864 bytes",
                    "parent_vm_hwm_kib": 12072,
                    "gnu_time_percent_m_kib": 75264,
                },
            },
            "memory_rule": "Keep Java VmHWM and GNU time wait-rusage values separate. They can describe overlapping work and are never added.",
            "standalone_reader_time": "not measured: Java consumes producer stdout while Flang is running, so subtracting producer wall time would not isolate decode work",
        },
        "commands": {
            "gradle_classpath_task": build_command,
            "java_invocation_shape": [
                "/usr/bin/time -f '%e\\t%M' -o HOST_TIME java -Xms256m -Xmx1g -cp CLASSPATH",
                MAIN_CLASS,
                "--mode=json|protobuf --source=SOURCE --plugin=LOCAL_PLUGIN --flang=GNU_TIME_WRAPPER --warmups=1",
            ],
            "child_time_invocation_shape": "/usr/bin/time -f '%e\\t%M' -a -o CHILD_TIME FLANG -fc1 -fopenmp -load PLUGIN -plugin ACTION SOURCE",
        },
        "provenance": {
            "git_head": git_output("rev-parse", "HEAD"),
            "git_status_before_measurements": git_status.splitlines(),
            "platform": platform.platform(),
            "python_version": sys.version.split()[0],
            "java_version": subprocess.run(["java", "-version"], capture_output=True, text=True).stderr.strip(),
            "flang": {"path": str(flang), "sha256": sha256_file(flang)},
            "plugins": {
                "json": {"path": str(json_plugin), "sha256": sha256_file(json_plugin)},
                "protobuf": {"path": str(protobuf_plugin), "sha256": sha256_file(protobuf_plugin)},
            },
            "source_hashes": {
                "hello.f90": sha256_file(SMALL_SOURCE),
                "polybench-3mm.f90": sha256_file(LARGE_SOURCE),
                "synthetic-assignment-chain-512.f90": hashlib.sha256(synthetic_bytes).hexdigest(),
            },
            "schema": {"path": str(SCHEMA), "sha256": sha256_file(SCHEMA)},
            "harness_hashes": {
                "java_entrypoint": {"path": str(JAVA_MAIN), "sha256": sha256_file(JAVA_MAIN)},
                "python_driver": {"path": str(Path(__file__).resolve()), "sha256": sha256_file(Path(__file__).resolve())},
                "gradle_build": {"path": str(PARSER_DIR / "build.gradle"), "sha256": sha256_file(PARSER_DIR / "build.gradle")},
            },
            "environment": {
                "SPECS_JAVA_LIBS_HOME": env.get("SPECS_JAVA_LIBS_HOME"),
                "LD_LIBRARY_PATH": env.get("LD_LIBRARY_PATH"),
                "java_heap_arguments": HEAP_ARGS,
            },
            "classpath_sha256": hashlib.sha256(classpath.encode("utf-8")).hexdigest(),
        },
        "parity_preflight": [],
        "measurements": [],
    }

    if gradle_output:
        report["build_output_tail"] = gradle_output[-3000:]

    with tempfile.TemporaryDirectory(prefix="flang-java-e2e-") as temporary:
        temp_dir = Path(temporary)
        wrapper = make_flang_wrapper(temp_dir)

        for workload, source in workloads:
            mode_records: dict[str, dict[str, Any]] = {}
            for mode, plugin in (("json", json_plugin), ("protobuf", protobuf_plugin)):
                invocation = invoke_java(
                    env=env,
                    classpath=classpath,
                    wrapper=wrapper,
                    temp_dir=temp_dir,
                    source=source,
                    mode=mode,
                    plugin=plugin,
                    warmups=0,
                    timed=False,
                )
                mode_records[mode] = invocation["records"][0]
            json_record = mode_records["json"]
            protobuf_record = mode_records["protobuf"]
            matched = (
                json_record["generated_code_sha256"] == protobuf_record["generated_code_sha256"]
                and json_record["generated_code_bytes"] == protobuf_record["generated_code_bytes"]
            )
            report["parity_preflight"].append(
                {
                    "workload": workload,
                    "source": source_metadata(source, workload),
                    "generated_code_equal": matched,
                    "json": json_record,
                    "protobuf": protobuf_record,
                }
            )
            if not matched:
                report["status"] = "parity_gate_failed"
                report["finished_utc"] = datetime.now(timezone.utc).isoformat()
                return report

        for workload, source in workloads:
            for repeat in range(args.repeats):
                modes = (("json", json_plugin), ("protobuf", protobuf_plugin))
                if repeat % 2:
                    modes = tuple(reversed(modes))
                for mode, plugin in modes:
                    invocation = invoke_java(
                        env=env,
                        classpath=classpath,
                        wrapper=wrapper,
                        temp_dir=temp_dir,
                        source=source,
                        mode=mode,
                        plugin=plugin,
                        warmups=args.warmups,
                        timed=True,
                    )
                    sample = next(record for record in invocation["records"] if record["phase"] == "sample")
                    warmups = [record for record in invocation["records"] if record["phase"] == "warmup"]
                    host = invocation["java_command_gnu_time"]
                    assert host is not None
                    process_peak = invocation["java_process_vm_hwm"]
                    report["measurements"].append(
                        {
                            "workload": workload,
                            "source_sha256": sha256_file(source),
                            "mode": mode,
                            "repeat": repeat + 1,
                            "warmup_records": warmups,
                            **sample,
                            "java_command_gnu_time_elapsed_seconds": host["gnu_time_elapsed_seconds"],
                            "java_command_gnu_time_wait_rusage_maxrss_kib": host[
                                "gnu_time_wait_rusage_maxrss_kib"
                            ],
                            "java_process_vm_hwm_kib": process_peak["java_process_vm_hwm_kib"],
                            "java_process_vm_hwm_status": process_peak["status"],
                            "java_process_vm_hwm_detail": process_peak["detail"],
                        }
                    )

    report["medians"] = [summarize(report["measurements"], label) for label, _ in workloads]
    report["finished_utc"] = datetime.now(timezone.utc).isoformat()
    report["status"] = "complete"
    return report


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--flang", required=True, help="Absolute path to the Flang 22 executable")
    parser.add_argument("--json-plugin", required=True, help="Local finalized JSON plugin")
    parser.add_argument("--protobuf-plugin", required=True, help="Local protobuf plugin")
    parser.add_argument("--repeats", type=int, default=5, help="Fresh JVM samples per mode/workload")
    parser.add_argument("--warmups", type=int, default=1, help="Unreported pipeline warmups in each JVM")
    parser.add_argument(
        "--output",
        type=Path,
        default=PARSER_DIR / "docs/native-end-to-end-benchmark-report.json",
        help="JSON report destination",
    )
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    try:
        report = run(args)
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(report, indent=2, sort_keys=True) + "\n", encoding="utf-8")
        print(f"report: {args.output.resolve()}")
        print(f"status: {report['status']}")
        if report["status"] == "complete":
            for item in report["medians"]:
                json_ns = item["modes"]["json"]["median_end_to_end_nanos"]
                protobuf_ns = item["modes"]["protobuf"]["median_end_to_end_nanos"]
                print(
                    f"{item['workload']}: end-to-end median JSON {json_ns / 1e6:.3f} ms, "
                    f"protobuf {protobuf_ns / 1e6:.3f} ms"
                )
            return 0
        return 2
    except Exception as error:
        print(f"benchmark failed: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
