package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranAstOptions;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.ProgramUnit;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.StringLines;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * R501 program
 * <p>
 * The root node of a Fortran application. Contains one or more ProgramUnit children.
 */
public class FortranFile extends FortranNode {
    public static final int COLUMN_LIMIT = 72;

    /**
     * The name of this file.
     */
    public static final DataKey<String> FILE_NAME = KeyFactory.string("fileName").setDefault(() -> "<no-filename>");

    /**
     * The name of the path corresponding to this file.
     */
    public static final DataKey<String> FOLDER_NAME = KeyFactory.string("folderName").setDefault(() -> "./");

    /**
     * The original source path that originated this file. If this source file was explicitly specified, returns the path to the source; if this source file was specified as being part of a source folder, returns the name of that folder.
     * <p>
     * For instance, if the path of the file is ./folder1/folder2/file.f90, and the input source path was ./folder1, this method returns ./folder1.
     */
    public static final DataKey<String> INPUT_SOURCE_PATH = KeyFactory.string("inputSourcePath");

    // Final comments of the program (which are, therefore, not associated with any statement)
    public static final DataKey<List<String>> FINAL_COMMENTS = KeyFactory.list("finalComments", String.class);

    public FortranFile(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<ProgramUnit> getProgramUnits() {
        return getChildren(ProgramUnit.class);
    }

    public List<String> getFinalComments() {
        return get(FINAL_COMMENTS);
    }

    @Override
    public String getCode() {
        // To output in the correct form, we set it in the context at the start
        var fileName = get(FILE_NAME);
        var fixedForm = List.of("f", "for").contains(SpecsIo.getExtension(fileName));
        getContext().set(FortranContext.FIXED_FORM, fixedForm);

        var finalComments = getFinalComments();

        var commentSuffix = finalComments.stream()
                .map(comment -> getCommentStarter() + comment + ln())
                .collect(Collectors.joining());

        var programUnitsCode = getProgramUnits().stream()
                .map(programUnit -> programUnit.getCode() + ln())
                .collect(Collectors.joining(ln()));

        var code = programUnitsCode + commentSuffix;
        if (fixedForm) {
            code = compactCode(code);
        }

        return code;
    }

    /**
     * Compacts the code by removing as many spaces as possible dividing lines when needed.
     * This is useful for making sure the code fits within the 72-character limit of fixed-form Fortran.
     *
     * @param code The code to compact.
     * @return The compacted code.
     */
    private String compactCode(String code) {
        return StringLines.getLines(code).stream()
                .map(this::compactLine)
                .collect(Collectors.joining(ln()));
    }

    /**
     * Compacts a line of code.
     * It removes spaces after the first 6 characters, except for spaces inside string literals, and divides lines
     * when they reach the 72th column to prevent overflow.
     *
     * @param line The line of code to compact.
     * @return The compacted line of code.
     */
    private String compactLine(String line) {
        if (line.length() <= 6) {
            return line;
        }

        var newLine = new StringBuilder(line.substring(0, 6));
        var stringDelimiter = getContext().getFortranOptions().get(FortranAstOptions.SINGLE_QUOTE_STRINGS)
                ? '\'' : '"';
        var inString = false;
        var lineSize = 6;

        for (var i = 6; i < line.length(); i++) {
            var c = line.charAt(i);
            if (c == stringDelimiter) {
                inString = !inString;
            }
            if (!inString && c == ' ') {
                continue;
            }

            if (lineSize == COLUMN_LIMIT) {
                newLine.append("\n     +");
                lineSize = 6;
            }

            newLine.append(c);
            lineSize++;
        }

        return newLine.toString();
    }
}
