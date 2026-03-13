#!/usr/bin/env python3
"""Metafor coverage pipeline for the Fujitsu compiler test suite.

Pipeline stages
---------------
1. Check which Fortran files parse with flang.
2. Check which Stage-1 files are accepted by DumpASTPlugin.so.
3. Run Metafor on Stage-2 files and locate the regenerated output.
4.1 Re-run Metafor on the Stage-3 output and require byte-for-byte stability.
4.2 Compile original and Stage-3 output to LLVM IR and compare normalized IR.

Outputs
-------
- results.csv: per-file status for every stage
- summary.json: machine-readable metrics for the run
- history.csv: appended run-level metrics for longitudinal tracking
- report.md: human-readable report
- logs/: stdout/stderr for commands that ran
- artifacts/: copied Stage-3 outputs and LLVM IR files for debugging

The script is designed for periodic execution so that coverage trends can be
tracked over time.
"""

from __future__ import annotations

import argparse
import csv
import hashlib
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
import time
from concurrent.futures import ThreadPoolExecutor, as_completed
from dataclasses import dataclass, field, asdict
from datetime import datetime, timezone
from pathlib import Path
from typing import Iterable, Optional


DEFAULT_NOOP_SCRIPT = """// No-op Metafor script used for round-trip testing.\n"""


@dataclass
class CommandResult:
    command: list[str]
    returncode: int
    stdout: str
    stderr: str
    elapsed_s: float
    cwd: Optional[str] = None


@dataclass
class FileResult:
    source_file: str
    relative_file: str
    file_size: int
    sha256: str

    stage1_ok: bool = False
    stage1_elapsed_s: float = 0.0
    stage1_note: str = ""

    stage2_ok: bool = False
    stage2_elapsed_s: float = 0.0
    stage2_note: str = ""

    stage3_ok: bool = False
    stage3_elapsed_s: float = 0.0
    stage3_note: str = ""
    stage3_output_file: str = ""

    stage41_ok: bool = False
    stage41_elapsed_s: float = 0.0
    stage41_note: str = ""
    stage41_output_file: str = ""

    stage42_ok: bool = False
    stage42_elapsed_s: float = 0.0
    stage42_note: str = ""
    stage42_original_ir: str = ""
    stage42_regenerated_ir: str = ""

    final_status: str = "pending"


class PipelineError(RuntimeError):
    pass


class MetaforFujitsuPipeline:
    def __init__(self, args: argparse.Namespace) -> None:
        self.args = args
        self.repo_root = Path(args.repo_root).resolve()
        self.fortran_root = (self.repo_root / "Fortran").resolve()
        self.output_dir = Path(args.output_dir).resolve()
        self.logs_dir = self.output_dir / "logs"
        self.artifacts_dir = self.output_dir / "artifacts"
        self.work_root = self.output_dir / "work"
        self.output_dir.mkdir(parents=True, exist_ok=True)
        self.logs_dir.mkdir(parents=True, exist_ok=True)
        self.artifacts_dir.mkdir(parents=True, exist_ok=True)
        self.work_root.mkdir(parents=True, exist_ok=True)
        self.noop_script_path = self.output_dir / "script.js"
        self.results: dict[str, FileResult] = {}
        self.run_started = datetime.now(timezone.utc)
        self.run_start_perf = time.perf_counter()

        if not self.fortran_root.is_dir():
            raise PipelineError(f"Fortran/ directory not found under {self.repo_root}")

        self.noop_script_path.write_text(DEFAULT_NOOP_SCRIPT, encoding="utf-8")

    def discover_files(self) -> list[Path]:
        files = sorted(self.fortran_root.rglob("*.f90"))
        if self.args.limit:
            files = files[: self.args.limit]
        return files

    def run(self) -> int:
        files = self.discover_files()
        if not files:
            raise PipelineError(f"No .f90 files found under {self.fortran_root}")

        for path in files:
            rel = path.relative_to(self.repo_root).as_posix()
            self.results[rel] = FileResult(
                source_file=str(path),
                relative_file=rel,
                file_size=path.stat().st_size,
                sha256=sha256_file(path),
            )

        self._run_stage(
            stage_name="stage1",
            items=files,
            worker=self._stage1,
            eligible=lambda _res: True,
        )
        self._run_stage(
            stage_name="stage2",
            items=files,
            worker=self._stage2,
            eligible=lambda res: res.stage1_ok,
        )
        self._run_stage(
            stage_name="stage3",
            items=files,
            worker=self._stage3,
            eligible=lambda res: res.stage2_ok,
        )
        self._run_stage(
            stage_name="stage41",
            items=files,
            worker=self._stage41,
            eligible=lambda res: res.stage3_ok,
        )
        self._run_stage(
            stage_name="stage42",
            items=files,
            worker=self._stage42,
            eligible=lambda res: res.stage3_ok,
        )

        for res in self.results.values():
            if res.stage42_ok and res.stage41_ok:
                res.final_status = "success"
            elif res.stage3_ok:
                res.final_status = "metafor-parsed"
            elif res.stage2_ok:
                res.final_status = "plugin-only"
            elif res.stage1_ok:
                res.final_status = "flang-only"
            else:
                res.final_status = "failed"

        self._write_outputs()
        return 0

    def _run_stage(self, stage_name: str, items: Iterable[Path], worker, eligible) -> None:
        futures = {}
        with ThreadPoolExecutor(max_workers=self.args.jobs) as pool:
            for path in items:
                res = self.results[path.relative_to(self.repo_root).as_posix()]
                if not eligible(res):
                    self._mark_skipped(stage_name, res)
                    continue
                futures[pool.submit(worker, path)] = path

            for future in as_completed(futures):
                path = futures[future]
                try:
                    future.result()
                except Exception as exc:  # noqa: BLE001
                    rel = path.relative_to(self.repo_root).as_posix()
                    res = self.results[rel]
                    self._mark_exception(stage_name, res, exc)

    def _stage1(self, path: Path) -> None:
        rel = path.relative_to(self.repo_root).as_posix()
        cmd = [self.args.flang, "-fsyntax-only", str(path)]
        cr = self._run_command(cmd, log_prefix=f"{safe_name(rel)}__stage1")
        res = self.results[rel]
        res.stage1_elapsed_s = cr.elapsed_s
        res.stage1_ok = cr.returncode == 0
        res.stage1_note = summarize_command_result(cr)

    def _stage2(self, path: Path) -> None:
        rel = path.relative_to(self.repo_root).as_posix()
        plugin_path = Path(self.args.dump_ast_plugin).resolve()
        cmd = [
            self.args.flang,
            "-fc1",
            "-load",
            str(plugin_path),
            "-plugin",
            self.args.dump_ast_plugin_name,
            str(path),
        ]
        cr = self._run_command(cmd, log_prefix=f"{safe_name(rel)}__stage2")
        res = self.results[rel]
        res.stage2_elapsed_s = cr.elapsed_s
        res.stage2_ok = cr.returncode == 0
        res.stage2_note = summarize_command_result(cr)

    def _stage3(self, path: Path) -> None:
        rel = path.relative_to(self.repo_root).as_posix()
        res = self.results[rel]
        work_dir = self._stage_work_dir(rel, "stage3")
        work_dir.mkdir(parents=True, exist_ok=True)
        cmd = self._metafor_command(path)
        cr = self._run_command(cmd, cwd=work_dir, log_prefix=f"{safe_name(rel)}__stage3")
        res.stage3_elapsed_s = cr.elapsed_s
        if cr.returncode != 0:
            res.stage3_ok = False
            res.stage3_note = summarize_command_result(cr)
            return

        woven_code = work_dir / "woven_code"
        generated = self._locate_generated_file(woven_code, path)
        if generated is None:
            res.stage3_ok = False
            res.stage3_note = "Metafor ran but no generated .f90 file was found in woven_code"
            return

        dest = self.artifacts_dir / safe_name(rel) / "stage3"
        dest.mkdir(parents=True, exist_ok=True)
        copied = dest / generated.name
        shutil.copy2(generated, copied)
        res.stage3_ok = True
        res.stage3_output_file = str(copied)
        res.stage3_note = f"Generated {generated.relative_to(work_dir)}"

    def _stage41(self, path: Path) -> None:
        rel = path.relative_to(self.repo_root).as_posix()
        res = self.results[rel]
        if not res.stage3_output_file:
            res.stage41_ok = False
            res.stage41_note = "Missing Stage 3 output"
            return

        stage3_output = Path(res.stage3_output_file).resolve()
        work_dir = self._stage_work_dir(rel, "stage41")
        work_dir.mkdir(parents=True, exist_ok=True)
        cmd = self._metafor_command(stage3_output)
        cr = self._run_command(cmd, cwd=work_dir, log_prefix=f"{safe_name(rel)}__stage41")
        res.stage41_elapsed_s = cr.elapsed_s
        if cr.returncode != 0:
            res.stage41_ok = False
            res.stage41_note = summarize_command_result(cr)
            return

        woven_code = work_dir / "woven_code"
        generated = self._locate_generated_file(woven_code, stage3_output)
        if generated is None:
            res.stage41_ok = False
            res.stage41_note = "Metafor ran on Stage 3 output but no generated file was found"
            return

        dest = self.artifacts_dir / safe_name(rel) / "stage41"
        dest.mkdir(parents=True, exist_ok=True)
        copied = dest / generated.name
        shutil.copy2(generated, copied)
        res.stage41_output_file = str(copied)

        same = files_are_identical(stage3_output, copied)
        res.stage41_ok = same
        res.stage41_note = "Exact byte-for-byte round trip match" if same else "Round trip mismatch"

    def _stage42(self, path: Path) -> None:
        rel = path.relative_to(self.repo_root).as_posix()
        res = self.results[rel]
        if not res.stage3_output_file:
            res.stage42_ok = False
            res.stage42_note = "Missing Stage 3 output"
            return

        stage3_output = Path(res.stage3_output_file).resolve()
        dest = self.artifacts_dir / safe_name(rel) / "stage42"
        dest.mkdir(parents=True, exist_ok=True)

        original_ir = dest / "original.ll"
        regenerated_ir = dest / "regenerated.ll"

        cr1 = self._run_command(
            self._flang_ir_command(path, original_ir),
            log_prefix=f"{safe_name(rel)}__stage42_original",
        )
        cr2 = self._run_command(
            self._flang_ir_command(stage3_output, regenerated_ir),
            log_prefix=f"{safe_name(rel)}__stage42_regenerated",
        )
        res.stage42_elapsed_s = cr1.elapsed_s + cr2.elapsed_s
        if cr1.returncode != 0 or cr2.returncode != 0:
            problems = []
            if cr1.returncode != 0:
                problems.append("original compile failed")
            if cr2.returncode != 0:
                problems.append("regenerated compile failed")
            res.stage42_ok = False
            res.stage42_note = ", ".join(problems)
            return

        res.stage42_original_ir = str(original_ir)
        res.stage42_regenerated_ir = str(regenerated_ir)

        same = self._normalized_ir_equal(original_ir, regenerated_ir)
        res.stage42_ok = same
        res.stage42_note = (
            "Normalized LLVM IR match"
            if same
            else "Normalized LLVM IR mismatch"
        )

    def _mark_skipped(self, stage_name: str, res: FileResult) -> None:
        if stage_name == "stage2":
            res.stage2_note = "skipped: Stage 1 failed"
        elif stage_name == "stage3":
            res.stage3_note = "skipped: Stage 2 failed"
        elif stage_name == "stage41":
            res.stage41_note = "skipped: Stage 3 failed"
        elif stage_name == "stage42":
            res.stage42_note = "skipped: Stage 3 failed"

    def _mark_exception(self, stage_name: str, res: FileResult, exc: Exception) -> None:
        note = f"internal error: {type(exc).__name__}: {exc}"
        if stage_name == "stage1":
            res.stage1_ok = False
            res.stage1_note = note
        elif stage_name == "stage2":
            res.stage2_ok = False
            res.stage2_note = note
        elif stage_name == "stage3":
            res.stage3_ok = False
            res.stage3_note = note
        elif stage_name == "stage41":
            res.stage41_ok = False
            res.stage41_note = note
        elif stage_name == "stage42":
            res.stage42_ok = False
            res.stage42_note = note

    def _run_command(
        self,
        cmd: list[str],
        cwd: Optional[Path] = None,
        log_prefix: str = "command",
    ) -> CommandResult:
        env = os.environ.copy()
        if self.args.node_path:
            env["NODE_PATH"] = self.args.node_path

        started = time.perf_counter()
        proc = subprocess.run(
            cmd,
            cwd=str(cwd) if cwd else None,
            env=env,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            timeout=self.args.timeout,
            check=False,
        )
        elapsed = time.perf_counter() - started
        result = CommandResult(
            command=cmd,
            returncode=proc.returncode,
            stdout=proc.stdout,
            stderr=proc.stderr,
            elapsed_s=elapsed,
            cwd=str(cwd) if cwd else None,
        )
        self._write_command_log(log_prefix, result)
        return result

    def _write_command_log(self, prefix: str, result: CommandResult) -> None:
        log_dir = self.logs_dir / prefix
        log_dir.mkdir(parents=True, exist_ok=True)
        (log_dir / "command.txt").write_text(shell_join(result.command), encoding="utf-8")
        (log_dir / "stdout.txt").write_text(result.stdout, encoding="utf-8")
        (log_dir / "stderr.txt").write_text(result.stderr, encoding="utf-8")
        (log_dir / "meta.json").write_text(
            json.dumps(
                {
                    "returncode": result.returncode,
                    "elapsed_s": result.elapsed_s,
                    "cwd": result.cwd,
                },
                indent=2,
            ),
            encoding="utf-8",
        )

    def _stage_work_dir(self, relative_file: str, stage_name: str) -> Path:
        return self.work_root / safe_name(relative_file) / stage_name

    def _metafor_command(self, source_file: Path) -> list[str]:
        return [
            self.args.npx,
            self.args.metafor_package,
            str(self.noop_script_path.resolve()),
            "-p",
            str(source_file.resolve()),
        ]

    def _flang_ir_command(self, source_file: Path, output_file: Path) -> list[str]:
        return [
            self.args.flang,
            "-S",
            "-emit-llvm",
            str(source_file),
            "-o",
            str(output_file),
        ]

    def _locate_generated_file(self, woven_code: Path, source_file: Path) -> Optional[Path]:
        if not woven_code.exists():
            return None

        candidates = sorted(woven_code.rglob(source_file.name))
        if len(candidates) == 1:
            return candidates[0]
        if len(candidates) > 1:
            exact_suffix = [c for c in candidates if c.as_posix().endswith(source_file.name)]
            if len(exact_suffix) == 1:
                return exact_suffix[0]
            return min(candidates, key=lambda p: len(p.as_posix()))

        all_f90 = sorted(woven_code.rglob("*.f90"))
        if len(all_f90) == 1:
            return all_f90[0]
        return None

    def _normalized_ir_equal(self, a_path: Path, b_path: Path) -> bool:
        a = normalize_llvm_ir(a_path.read_text(encoding="utf-8", errors="replace"))
        b = normalize_llvm_ir(b_path.read_text(encoding="utf-8", errors="replace"))
        return a == b

    def _write_outputs(self) -> None:
        results_csv = self.output_dir / "results.csv"
        summary_json = self.output_dir / "summary.json"
        report_md = self.output_dir / "report.md"
        history_csv = self.output_dir / "history.csv"

        rows = [asdict(r) for _, r in sorted(self.results.items())]
        write_csv(results_csv, rows)

        summary = self._build_summary()
        summary_json.write_text(json.dumps(summary, indent=2), encoding="utf-8")
        self._append_history(history_csv, summary)
        report_md.write_text(self._build_report(summary), encoding="utf-8")

    def _build_summary(self) -> dict:
        total_files = len(self.results)
        stage1_pass = sum(r.stage1_ok for r in self.results.values())
        stage2_pass = sum(r.stage2_ok for r in self.results.values())
        stage3_pass = sum(r.stage3_ok for r in self.results.values())
        stage41_pass = sum(r.stage41_ok for r in self.results.values())
        stage42_pass = sum(r.stage42_ok for r in self.results.values())
        end_perf = time.perf_counter()

        summary = {
            "timestamp_utc": self.run_started.isoformat(),
            "repo_root": str(self.repo_root),
            "fortran_root": str(self.fortran_root),
            "total_files": total_files,
            "stage1_pass": stage1_pass,
            "stage2_pass": stage2_pass,
            "stage3_pass": stage3_pass,
            "stage41_pass": stage41_pass,
            "stage42_pass": stage42_pass,
            "stage1_pass_rate_total": ratio(stage1_pass, total_files),
            "stage2_pass_rate_total": ratio(stage2_pass, total_files),
            "stage3_pass_rate_total": ratio(stage3_pass, total_files),
            "stage41_pass_rate_total": ratio(stage41_pass, total_files),
            "stage42_pass_rate_total": ratio(stage42_pass, total_files),
            "stage2_pass_rate_of_stage1": ratio(stage2_pass, stage1_pass),
            "stage3_pass_rate_of_stage2": ratio(stage3_pass, stage2_pass),
            "stage41_pass_rate_of_stage3": ratio(stage41_pass, stage3_pass),
            "stage42_pass_rate_of_stage3": ratio(stage42_pass, stage3_pass),
            "complete_successes": sum(
                r.stage1_ok and r.stage2_ok and r.stage3_ok and r.stage41_ok and r.stage42_ok
                for r in self.results.values()
            ),
            "elapsed_s": end_perf - self.run_start_perf,
            "configuration": {
                "flang": self.args.flang,
                "dump_ast_plugin": str(Path(self.args.dump_ast_plugin).resolve()),
                "dump_ast_plugin_name": self.args.dump_ast_plugin_name,
                "npx": self.args.npx,
                "metafor_package": self.args.metafor_package,
                "jobs": self.args.jobs,
                "timeout": self.args.timeout,
            },
            "notes": {
                "stage1": "Uses flang -fsyntax-only as a parse/syntax acceptance check.",
                "stage42": "LLVM IR comparison uses normalization to ignore known non-semantic headers and metadata.",
            },
        }
        return summary

    def _append_history(self, history_csv: Path, summary: dict) -> None:
        row = {
            "timestamp_utc": summary["timestamp_utc"],
            "total_files": summary["total_files"],
            "stage1_pass": summary["stage1_pass"],
            "stage2_pass": summary["stage2_pass"],
            "stage3_pass": summary["stage3_pass"],
            "stage41_pass": summary["stage41_pass"],
            "stage42_pass": summary["stage42_pass"],
            "complete_successes": summary["complete_successes"],
            "stage1_pass_rate_total": summary["stage1_pass_rate_total"],
            "stage2_pass_rate_total": summary["stage2_pass_rate_total"],
            "stage3_pass_rate_total": summary["stage3_pass_rate_total"],
            "stage41_pass_rate_total": summary["stage41_pass_rate_total"],
            "stage42_pass_rate_total": summary["stage42_pass_rate_total"],
            "elapsed_s": summary["elapsed_s"],
        }
        exists = history_csv.exists()
        with history_csv.open("a", newline="", encoding="utf-8") as fh:
            writer = csv.DictWriter(fh, fieldnames=list(row.keys()))
            if not exists:
                writer.writeheader()
            writer.writerow(row)

    def _build_report(self, summary: dict) -> str:
        failures_stage3 = [r.relative_file for r in self.results.values() if r.stage2_ok and not r.stage3_ok][:20]
        failures_stage41 = [r.relative_file for r in self.results.values() if r.stage3_ok and not r.stage41_ok][:20]
        failures_stage42 = [r.relative_file for r in self.results.values() if r.stage3_ok and not r.stage42_ok][:20]

        return f"""# Metafor Fujitsu Pipeline Report

Generated: {summary['timestamp_utc']}

## Overview

- Total `.f90` files discovered: {summary['total_files']}
- Stage 1 pass: {summary['stage1_pass']} ({pct(summary['stage1_pass_rate_total'])})
- Stage 2 pass: {summary['stage2_pass']} ({pct(summary['stage2_pass_rate_total'])})
- Stage 3 pass: {summary['stage3_pass']} ({pct(summary['stage3_pass_rate_total'])})
- Stage 4.1 pass: {summary['stage41_pass']} ({pct(summary['stage41_pass_rate_total'])})
- Stage 4.2 pass: {summary['stage42_pass']} ({pct(summary['stage42_pass_rate_total'])})
- Complete successes: {summary['complete_successes']}
- Total elapsed time: {summary['elapsed_s']:.2f}s

## Conditional funnel

- Stage 2 / Stage 1: {pct(summary['stage2_pass_rate_of_stage1'])}
- Stage 3 / Stage 2: {pct(summary['stage3_pass_rate_of_stage2'])}
- Stage 4.1 / Stage 3: {pct(summary['stage41_pass_rate_of_stage3'])}
- Stage 4.2 / Stage 3: {pct(summary['stage42_pass_rate_of_stage3'])}

## Interpretation

The most important long-term metric for Metafor coverage is usually **Stage 3 pass rate over total files**,
because it indicates how much of the Fujitsu benchmark suite can be parsed and regenerated. Stage 4.1 is a
strict idempotence check, while Stage 4.2 is a semantic-preservation proxy using normalized LLVM IR.

## Sample failures

### Stage 3 failures (up to 20)
{newline_join_bullets(failures_stage3)}

### Stage 4.1 failures (up to 20)
{newline_join_bullets(failures_stage41)}

### Stage 4.2 failures (up to 20)
{newline_join_bullets(failures_stage42)}

## Produced files

- `results.csv`: per-file results for all stages
- `summary.json`: machine-readable metrics for automation
- `history.csv`: append-only run summary for trend tracking
- `logs/`: stdout/stderr for every executed command
- `artifacts/`: regenerated files and LLVM IR files for debugging

## Assumptions

1. Stage 1 uses `flang -fsyntax-only` so the check focuses on parsing/syntax acceptance instead of linking.
2. Stage 4.2 compares normalized LLVM IR and intentionally strips selected non-semantic headers and metadata.
3. Metafor is executed in a per-file isolated working directory so each `woven_code/` tree is preserved.
"""


def sha256_file(path: Path) -> str:
    h = hashlib.sha256()
    with path.open("rb") as fh:
        for chunk in iter(lambda: fh.read(1024 * 1024), b""):
            h.update(chunk)
    return h.hexdigest()


def files_are_identical(a: Path, b: Path) -> bool:
    return sha256_file(a) == sha256_file(b)


def summarize_command_result(cr: CommandResult) -> str:
    if cr.returncode == 0:
        return "ok"
    stderr = (cr.stderr or "").strip().splitlines()
    stdout = (cr.stdout or "").strip().splitlines()
    detail = stderr[-1] if stderr else (stdout[-1] if stdout else "command failed")
    detail = detail[:400]
    return f"rc={cr.returncode}: {detail}"


def safe_name(text: str) -> str:
    return re.sub(r"[^A-Za-z0-9_.-]+", "__", text)


def shell_join(parts: list[str]) -> str:
    return " ".join(shlex_quote(p) for p in parts)


def shlex_quote(s: str) -> str:
    if re.fullmatch(r"[A-Za-z0-9_./:=+-]+", s):
        return s
    return "'" + s.replace("'", "'\\''") + "'"


def ratio(num: int, den: int) -> float:
    return 0.0 if den == 0 else num / den


def pct(x: float) -> str:
    return f"{100.0 * x:.2f}%"


def newline_join_bullets(items: list[str]) -> str:
    if not items:
        return "- None"
    return "\n".join(f"- `{item}`" for item in items)


def write_csv(path: Path, rows: list[dict]) -> None:
    if not rows:
        path.write_text("", encoding="utf-8")
        return
    fieldnames = list(rows[0].keys())
    with path.open("w", newline="", encoding="utf-8") as fh:
        writer = csv.DictWriter(fh, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)


LLVM_IR_DROP_LINE_PATTERNS = [
    re.compile(r"^; ModuleID = "),
    re.compile(r'^source_filename = '),
    re.compile(r"^!llvm\\.(module\\.flags|ident) = "),
    re.compile(r"^!\\d+ = "),
    re.compile(r"^attributes #\\d+ = "),
]


LLVM_IR_REPLACEMENTS = [
    (re.compile(r";.*$"), ""),
    (re.compile(r"#[0-9]+"), "#ATTR"),
    (re.compile(r"!dbg ![0-9]+"), ""),
    (re.compile(r",? !tbaa ![0-9]+"), ""),
    (re.compile(r",? !alias.scope ![0-9]+"), ""),
    (re.compile(r",? !noalias ![0-9]+"), ""),
    (re.compile(r",? !llvm.loop ![0-9]+"), ""),
]


def normalize_llvm_ir(text: str) -> str:
    lines = []
    for raw_line in text.splitlines():
        line = raw_line.rstrip()
        if any(p.search(line) for p in LLVM_IR_DROP_LINE_PATTERNS):
            continue
        for pattern, repl in LLVM_IR_REPLACEMENTS:
            line = pattern.sub(repl, line)
        line = re.sub(r"\\s+", " ", line).strip()
        if not line:
            continue
        lines.append(line)
    return "\n".join(lines)


def build_arg_parser() -> argparse.ArgumentParser:
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("repo_root", help="Path to the cloned Fujitsu compiler-test-suite repository")
    p.add_argument(
        "--output-dir",
        default="metafor_fujitsu_results",
        help="Directory where CSV/report/logs/artifacts will be written",
    )
    p.add_argument("--flang", default="flang-20", help="flang executable")
    p.add_argument(
        "--dump-ast-plugin",
        default="./build/DumpASTPlugin.so",
        help="Path to DumpASTPlugin.so",
    )
    p.add_argument(
        "--dump-ast-plugin-name",
        default="dump-ast",
        help="Plugin name passed to -plugin",
    )
    p.add_argument("--npx", default="npx", help="npx executable")
    p.add_argument(
        "--metafor-package",
        default="metafor",
        help="Package/executable name used after npx",
    )
    p.add_argument("--jobs", type=int, default=max(1, (os.cpu_count() or 2) // 2), help="Parallel workers")
    p.add_argument("--timeout", type=int, default=120, help="Per-command timeout in seconds")
    p.add_argument("--limit", type=int, default=0, help="Optional cap on number of .f90 files to process")
    p.add_argument("--node-path", default="", help="Optional NODE_PATH value")
    return p


def main() -> int:
    args = build_arg_parser().parse_args()
    try:
        pipeline = MetaforFujitsuPipeline(args)
        return pipeline.run()
    except subprocess.TimeoutExpired as exc:
        print(f"ERROR: command timed out after {exc.timeout}s", file=sys.stderr)
        return 124
    except PipelineError as exc:
        print(f"ERROR: {exc}", file=sys.stderr)
        return 2


if __name__ == "__main__":
    raise SystemExit(main())
