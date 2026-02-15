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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FortranNativeParser {

    private static final boolean SAVE_JSON = true;

    private static final WebResourceProvider LINUX_DUMPER =
            WebResourceProvider.newInstance("https://github.com/specs-feup/flang-dumper/releases/download/plugin_dump_ast_v1.0.3/",
                    "DumpASTPlugin.so", "v1.0.3");

    private static final Lazy<File> FLANG_DUMPER = Lazy.newInstance(FortranNativeParser::prepareDumper);
    private static final Lazy<File> TEMP_FOLDER = Lazy.newInstance(() -> SpecsIo.getTempFolder("metafor"));

    private final FortranContext context;

    public FortranNativeParser(FortranContext context) {
        this.context = context;
    }

    public FortranJsonResult parse(File file) {

        context.set(FortranContext.LAST_PARSED_FILE, Optional.of(file));

        var plugin = FLANG_DUMPER.get();
        System.out.println("PLUGIN : " + plugin.getAbsolutePath());

        // Execute flang to obtain json
        var command = List.of("flang-20", "-fc1", "-load", plugin.getAbsolutePath(), "-plugin", "dump-ast", file.getAbsolutePath());

        var jsonFile = SAVE_JSON ? new File(file.getAbsoluteFile().getParentFile(), file.getName() + ".json") : null;

        // Use runProcess that uses outputProcessor, to process json as a stream
        Function<InputStream, FortranJsonResult> outputProcessor = (stdout) -> parseStream(stdout, jsonFile);
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

        // Check if flang-20 is available
        SpecsSystem.isCommandAvailable(List.of("flang-20"), new File("."));


        // Resolve filename (taking into account versioning)
        var resourceWithVersion = LINUX_DUMPER.createResourceVersion("_" + LINUX_DUMPER.version());
        var pluginFile = new File(TEMP_FOLDER.get(), resourceWithVersion.getFilename());


        // Check if file is in place
        // If donwload failed previously, size might be zero
        if (pluginFile.isFile() && pluginFile.length() != 0) {
            return pluginFile;
        }

        // If file does not exist, create file and return
        return resourceWithVersion.write(TEMP_FOLDER.get());
    }
}

