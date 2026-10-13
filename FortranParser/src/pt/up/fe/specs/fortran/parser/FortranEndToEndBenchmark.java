package pt.up.fe.specs.fortran.parser;

import com.google.gson.Gson;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.util.SpecsSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * One-process measurement entry point for the local JSON and protobuf native parsers.
 * The Python driver starts a fresh JVM for each repeat and parses the BENCH_RESULT lines.
 */
public final class FortranEndToEndBenchmark {

    private static final Gson GSON = new Gson();

    private FortranEndToEndBenchmark() {
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> options = parseOptions(args);
        String mode = required(options, "mode");
        String sourcePath = required(options, "source");
        String pluginPath = required(options, "plugin");
        String flangCommand = required(options, "flang");
        int warmups = Integer.parseInt(options.getOrDefault("warmups", "1"));
        if (warmups < 0) {
            throw new IllegalArgumentException("--warmups must be at least zero");
        }

        String action = switch (mode) {
            case "json" -> "dump-ast";
            case "protobuf" -> "dump-ast-protobuf";
            default -> throw new IllegalArgumentException("Unsupported mode: " + mode);
        };

        File source = new File(sourcePath).getCanonicalFile();
        File plugin = new File(pluginPath).getCanonicalFile();
        if (!source.isFile()) {
            throw new IllegalArgumentException("Source file does not exist: " + source);
        }
        if (!plugin.isFile()) {
            throw new IllegalArgumentException("Plugin file does not exist: " + plugin);
        }

        SpecsSystem.programStandardInit();
        FortranNativeParser parser = new FortranNativeParser(new FortranContext(), plugin,
                flangCommand, action);

        for (int index = 0; index < warmups; index++) {
            runPipeline(parser, source, mode, "warmup", index);
        }
        runPipeline(parser, source, mode, "sample", 0);

        System.out.println("BENCH_PROCESS\t" + GSON.toJson(readProcessPeakRss()));
    }

    private static Map<String, Object> readProcessPeakRss() {
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("measurement", "java_process_vm_hwm");

        if (!System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("linux")) {
            record.put("java_process_vm_hwm_kib", null);
            record.put("status", "unsupported");
            record.put("detail", "/proc/self/status VmHWM is available only on Linux");
            return record;
        }

        try (var lines = Files.lines(Path.of("/proc/self/status"), StandardCharsets.US_ASCII)) {
            String vmHwm = lines.filter(line -> line.startsWith("VmHWM:")).findFirst().orElse(null);
            if (vmHwm == null) {
                return unsupportedProcessPeak(record, "VmHWM was absent from /proc/self/status");
            }

            String[] fields = vmHwm.substring("VmHWM:".length()).trim().split("\\s+");
            if (fields.length != 2 || !fields[1].equals("kB")) {
                return unsupportedProcessPeak(record, "Could not parse /proc/self/status VmHWM: " + vmHwm);
            }

            record.put("java_process_vm_hwm_kib", Long.parseLong(fields[0]));
            record.put("status", "supported");
            record.put("detail", "Read from /proc/self/status after all warmups and the sample");
            return record;
        } catch (IOException | NumberFormatException error) {
            return unsupportedProcessPeak(record,
                    "Could not read /proc/self/status VmHWM: " + error.getMessage());
        }
    }

    private static Map<String, Object> unsupportedProcessPeak(Map<String, Object> record, String detail) {
        record.put("java_process_vm_hwm_kib", null);
        record.put("status", "unsupported");
        record.put("detail", detail);
        return record;
    }

    private static void runPipeline(FortranNativeParser parser, File source, String mode,
            String phase, int iteration) throws Exception {
        long endToEndStart = System.nanoTime();

        long parseStart = System.nanoTime();
        FortranJsonResult parseResult = parser.parse(source);
        long parseNanos = System.nanoTime() - parseStart;

        long astBuildStart = System.nanoTime();
        FortranNode root = new FortranAstBuilder(parseResult).build();
        long astBuildNanos = System.nanoTime() - astBuildStart;

        long codegenStart = System.nanoTime();
        String generatedCode = root.getCode();
        long getCodeNanos = System.nanoTime() - codegenStart;

        long endToEndNanos = System.nanoTime() - endToEndStart;
        byte[] codeBytes = generatedCode.getBytes(StandardCharsets.UTF_8);

        Map<String, Object> record = new LinkedHashMap<>();
        record.put("phase", phase);
        record.put("iteration", iteration);
        record.put("mode", mode);
        record.put("parse_nanos", parseNanos);
        record.put("ast_build_nanos", astBuildNanos);
        record.put("get_code_nanos", getCodeNanos);
        record.put("end_to_end_nanos", endToEndNanos);
        record.put("generated_code_bytes", codeBytes.length);
        record.put("generated_code_sha256", sha256(codeBytes));
        System.out.println("BENCH_RESULT\t" + GSON.toJson(record));
    }

    private static Map<String, String> parseOptions(String[] args) {
        Map<String, String> options = new LinkedHashMap<>();
        for (String arg : args) {
            if (!arg.startsWith("--") || !arg.contains("=")) {
                throw new IllegalArgumentException("Expected --name=value, got: " + arg);
            }
            int separator = arg.indexOf('=');
            String key = arg.substring(2, separator);
            String value = arg.substring(separator + 1);
            if (options.put(key, value) != null) {
                throw new IllegalArgumentException("Option supplied more than once: --" + key);
            }
        }
        return options;
    }

    private static String required(Map<String, String> options, String key) {
        String value = options.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required option --" + key);
        }
        return value;
    }

    private static String sha256(byte[] bytes) throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(bytes);
        StringBuilder hex = new StringBuilder(digest.length * 2);
        for (byte value : digest) {
            hex.append(String.format("%02x", value));
        }
        return hex.toString();
    }
}
