package pt.up.fe.specs.fortran.weaver;

import org.lara.interpreter.joptions.config.interpreter.LaraiKeys;
import org.lara.interpreter.weaver.interf.AGear;
import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.interf.WeaverEngine;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.lara.interpreter.weaver.options.WeaverOptionUtils;
import org.lara.interpreter.weaver.utils.SourcesGatherer;
import org.lara.language.specification.dsl.LanguageSpecification;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranAstOptions;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.FortranNodeFactory;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Application;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.parser.ApplicationParser;
import pt.up.fe.specs.fortran.weaver.abstracts.weaver.AFortranWeaver;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Weaver Implementation for FortranWeaver<br>
 * Since the generated abstract classes are always overwritten, their implementation should be done by extending those abstract classes with user-defined classes.<br>
 * The abstract class {@link pt.up.fe.specs.fortran.weaver.abstracts.AFortranWeaverJoinPoint} contains attributes and actions common to all join points.
 *
 * @author Lara Weaver Generator
 */
public class FortranWeaver extends AFortranWeaver {

    private final static String WOVEN_CODE_DIR = "woven_code";

    private List<File> currentSources;
    private DataStore currentArgs;

    private Map<File, File> allSourceFiles;
    private Application currentRoot;

    public FortranWeaver() {
        this.currentSources = Collections.emptyList();
        this.currentArgs = new SimpleDataStore(getStoreDefinition());

        this.allSourceFiles = Collections.emptyMap();
        this.currentRoot = null;
    }

    /**
     * Setups the weaver with inputs sources, an output folder and the provided options.
     *
     * @param sources   the sources with the code (files/folders)
     * @param outputDir output folder for the generated file(s)
     * @param args      options for the weaver
     * @return true if initialization occurred without problems, false otherwise
     */
    @Override
    public boolean begin(List<File> sources, File outputDir, DataStore args) {

        // Set fields
        this.currentSources = sources;

        //this.currentOutputDir = outputDir;
        this.currentArgs = args;

        this.allSourceFiles = SourcesGatherer.build(sources, List.of("json", "f90", "f", "for")).getSourceFiles();

        var fortranOptions = args;

        // Create root node
        this.currentRoot = new ApplicationParser(fortranOptions).parse(allSourceFiles);

        return true;

    }

    /**
     * Return a JoinPoint instance of the sources root, i.e., an instance of AProgram
     *
     * @return an instance of the join point root/program
     */
    @Override
    public JoinPoint getRootJp() {
        return FortranJoinpoints.create(currentRoot);
    }

    /**
     * Performs operations needed when closing the weaver (e.g., generates new version of source file(s) to the specified output folder).
     *
     * @return if close was successful
     */
    @Override
    public boolean close() {

        var baseOutputFolder = currentArgs.hasValue(LaraiKeys.OUTPUT_FOLDER) ? currentArgs.get(LaraiKeys.OUTPUT_FOLDER) :
                new File("./");

        var outputFolder = new File(baseOutputFolder, WOVEN_CODE_DIR);

        // Make sure output folder exists
        SpecsIo.mkdir(outputFolder);

        // Write output files
        for (var file : currentRoot.getFiles()) {
            // Get relative path
            var filename = file.get(FortranFile.FILE_NAME);
            filename = SpecsIo.getExtension(filename).equals("json") ? SpecsIo.removeExtension(filename) + ".f90" : filename;
            var folder = new File(file.get(FortranFile.FOLDER_NAME));
            var inputSourcePath = new File(file.get(FortranFile.INPUT_SOURCE_PATH));


            var baseOutputFile = inputSourcePath.isFile() ? filename : SpecsIo.getRelativePath(folder, inputSourcePath) + "/" + filename;

            var outputFile = new File(outputFolder, baseOutputFile);
            SpecsLogs.info("Writing file '" + outputFile.getAbsolutePath() + "'");
            SpecsIo.write(outputFile, file.getCode());
        }

        SpecsLogs.info("Output files written to folder " + outputFolder.getAbsolutePath());

        return true;
    }

    /**
     * Returns a list of Gears associated to this weaver engine
     *
     * @return a list of implementations of {@link AGear} or null if no gears are available
     */
    @Override
    public List<AGear> getGears() {
        return List.of(); //i.e., no gears currently being used
    }

    /**
     * Returns a list of options specific to this weaver engine.
     *
     * @return a list of {@link WeaverOption} representing additional options provided by this weaver
     */
    @Override
    public List<WeaverOption> getOptions() {
        return WeaverOptionUtils.toWeaverOption(FortranAstOptions.STORE_DEFINITION);
    }

    /**
     * Returns thread-local instance of weaver engine.
     */
    public static FortranWeaver getFortranWeaver() {
        return (FortranWeaver) WeaverEngine.getThreadLocalWeaver();
    }

    /**
     * Builds the language specification, based on the input XML files.
     *
     * @return a new {@link LanguageSpecification} instance for this weaver
     */
    public static LanguageSpecification buildLanguageSpecification() {
        return LanguageSpecification.newInstance(() -> "fortran/weaverspecs/" + LanguageSpecification.getJoinPointsFilename(),
                () -> "fortran/weaverspecs/" + LanguageSpecification.getAttributesFilename(),
                () -> "fortran/weaverspecs/" + LanguageSpecification.getActionsFilename());
    }

    /**
     * Builds the language specification, based on the input XML files.
     *
     * @return a new {@link LanguageSpecification} instance for this weaver
     */
    @Override
    protected LanguageSpecification buildLangSpecs() {
        return buildLanguageSpecification();
    }

    public static FortranNodeFactory getFactory() {
        return getContext().get(FortranContext.FACTORY);
    }

    public static FortranContext getContext() {
        return getFortranWeaver().currentRoot.get(FortranNode.CONTEXT);
    }
}
