package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.WebResourceProvider;
import pt.up.fe.specs.util.system.OutputType;
import pt.up.fe.specs.util.system.StreamToString;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FortranNativeParser {
    public static final int FLANG_VERSION = 22;

    private static final boolean USE_RELEASE = false;

    private static final boolean SAVE_JSON = false;

    private static final String BASE_URL = "https://github.com/specs-feup/flang-dumper/releases/download/";
    private static final String CURRENT_VERSION = "v22.0.0";

    private static final WebResourceProvider LINUX_DUMPER_NIGHTLY =
            WebResourceProvider.newInstance(BASE_URL + "nightly/",
                    "DumpASTPlugin.so");

    private static final WebResourceProvider LINUX_DUMPER_RELEASE =
            WebResourceProvider.newInstance(BASE_URL + CURRENT_VERSION + "/",
                    "DumpASTPlugin.so", CURRENT_VERSION);

    private static final Lazy<File> FLANG_DUMPER = Lazy.newInstance(FortranNativeParser::prepareDumper);
    private static final Lazy<File> TEMP_FOLDER = Lazy.newInstance(() -> SpecsIo.getTempFolder("metafor"));

    private final FortranContext context;
    private final File localPlugin;
    private final String localFlangCommand;
    private final String localPluginAction;
    private final boolean protobufMode;

    public FortranNativeParser(FortranContext context) {
        this.context = context;
        this.localPlugin = null;
        this.localFlangCommand = null;
        this.localPluginAction = null;
        this.protobufMode = false;
    }

    /**
     * Creates an experimental parser that loads a local Flang protobuf plugin.
     * This constructor bypasses the default remote JSON dumper setup.
     *
     * @param context the Fortran parsing context
     * @param protobufPlugin local binary protobuf plugin file
     * @param flangCommand Flang command to execute
     */
    public FortranNativeParser(FortranContext context, File protobufPlugin, String flangCommand) {
        this(context, protobufPlugin, flangCommand, "dump-ast-protobuf");
    }

    /**
     * Creates a parser for an explicitly selected local dumper action. Package-private so
     * parity tests can exercise both the JSON and experimental protobuf plugins without
     * changing the production JSON default.
     */
    FortranNativeParser(FortranContext context, File localPlugin, String flangCommand, String pluginAction) {
        if (localPlugin == null || !localPlugin.exists() || !localPlugin.isFile()) {
            throw new IllegalArgumentException("The native plugin must be an existing file: " + localPlugin);
        }

        if (flangCommand == null || flangCommand.isBlank()) {
            throw new IllegalArgumentException("The Flang command must not be null or blank");
        }

        if (!"dump-ast".equals(pluginAction) && !"dump-ast-protobuf".equals(pluginAction)) {
            throw new IllegalArgumentException("Unsupported native dumper action: " + pluginAction);
        }

        this.context = context;
        this.localPlugin = localPlugin;
        this.localFlangCommand = flangCommand;
        this.localPluginAction = pluginAction;
        this.protobufMode = "dump-ast-protobuf".equals(pluginAction);
    }

    private static String getFlangCommand() {
        return "flang-" + FLANG_VERSION;
    }

    public FortranJsonResult parse(File file) {

        context.set(FortranContext.LAST_PARSED_FILE, Optional.of(file));

        var plugin = localPlugin == null ? FLANG_DUMPER.get() : localPlugin;
        System.out.println("PLUGIN : " + plugin.getAbsolutePath());

        // Execute flang to obtain JSON or protobuf output.
        var flangCommand = localFlangCommand == null ? getFlangCommand() : localFlangCommand;
        var action = localPluginAction == null ? "dump-ast" : localPluginAction;
        var command = List.of(flangCommand, "-fc1", "-fopenmp", "-load", plugin.getAbsolutePath(), "-plugin", action, file.getAbsolutePath());

        var jsonFile = protobufMode ? null
                : (SAVE_JSON ? new File(file.getAbsoluteFile().getParentFile(), file.getName() + ".json") : null);

        // Use runProcess to process parser output directly from stdout.
        Function<InputStream, FortranJsonResult> outputProcessor = protobufMode
                ? stdout -> parseProtobufStream(stdout, context)
                : stdout -> parseStream(stdout, jsonFile);
        Function<InputStream, String> stderrProcessor = new StreamToString(false, true, OutputType.StdErr);

        //var flangExecution = SpecsSystem.runProcess(command, TEMP_FOLDER.get(), true, false);
        var flangExecution = SpecsSystem.runProcess(command, TEMP_FOLDER.get(), outputProcessor, stderrProcessor);

        if (flangExecution.getReturnValue() != 0) {
            throw new RuntimeException("Problems executing flang: " + flangExecution.getStdErr());
        }

        if (flangExecution.getOutputException().isPresent()) {
            throw new RuntimeException(flangExecution.getOutputException().get());
        }

        return flangExecution.getStdOut();
        //return FortranJsonParser.parse(new StringReader(flangExecution.getOutput()), context);
    }

    private static FortranJsonResult parseProtobufStream(InputStream stdout, FortranContext context) {
        try {
            return FortranProtobufParser.parse(stdout, context);
        } catch (RuntimeException parseFailure) {
            // SpecsSystem waits for the process before it observes this parser future. Keep
            // draining stdout so a producer with more output than the pipe buffer can exit.
            try {
                stdout.transferTo(OutputStream.nullOutputStream());
            } catch (IOException drainFailure) {
                parseFailure.addSuppressed(drainFailure);
            }

            throw parseFailure;
        }
    }

    private FortranJsonResult parseStream(InputStream stream, File jsonOutput) {
        if (jsonOutput == null) {
            return FortranJsonParser.parse(new InputStreamReader(stream), context);
        }

        // Read the stream to a string first
        var json = SpecsIo.read(stream);

        SpecsIo.write(jsonOutput, json);
        SpecsLogs.info("Wrote JSON output at '" + jsonOutput.getAbsolutePath() + "'");
        return FortranJsonParser.parse(new StringReader(json), context);
    }

    public FortranJsonResult parse(InputStream input) {
        // Create temporary file for dumping the input
        var tempFile = SpecsIo.getTempFile("", "f90");
        var code = SpecsIo.read(input);
        SpecsIo.write(tempFile, code);
        var result = parse(tempFile);
        SpecsIo.delete(tempFile);
        return result;
    }


    private static File prepareDumper() {

        // Check if Linux
        if (!SpecsSystem.isLinux()) {
            //SpecsLogs.info("Fortran input files only supported in Linux operating system, detected " + System.getProperty("os.name"));
            throw new RuntimeException("Fortran input files only supported in Linux operating system, detected " + System.getProperty("os.name"));
        }

        // Check if flang is available
        SpecsSystem.isCommandAvailable(List.of(getFlangCommand()), new File("."));

        // Resolve filename (taking into account versioning)
        var resource = USE_RELEASE ? LINUX_DUMPER_RELEASE.createResourceVersion("_" + LINUX_DUMPER_RELEASE.version())
                : LINUX_DUMPER_NIGHTLY;

        var pluginFile = new File(TEMP_FOLDER.get(), resource.getFilename());


        // Check if file is in place
        // If download failed previously, size might be zero
        if (pluginFile.isFile() && pluginFile.length() != 0) {
            return pluginFile;
        }

        // If file does not exist, create file and return
        return resource.write(TEMP_FOLDER.get());
    }
}
