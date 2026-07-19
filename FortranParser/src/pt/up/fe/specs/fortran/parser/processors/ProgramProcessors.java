package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;
import pt.up.fe.specs.util.SpecsIo;

import java.util.List;

public class ProgramProcessors extends ANodeProcessor {


    public ProgramProcessors(FortranJsonResult data) {
        super(data);
    }

    public void fortranFile(FortranFile fortranFile) {
        fortranFile.setChildren(getChildren(fortranFile, FlangName.PROGRAM_UNIT));

        var context = fortranFile.getContext();
        var lastParsedFile = context.get(FortranContext.LAST_PARSED_FILE);

        lastParsedFile.ifPresent(parsedFile -> {
            var fileName = parsedFile.getName();

            if (fileName.endsWith(".json")) {
                var fileExtension = context.get(FortranContext.LAST_PARSED_FILE_EXT).orElseGet(() -> "f90");
                fileName = SpecsIo.removeExtension(fileName) + "." + fileExtension;
            }

            fortranFile.set(FortranFile.FILE_NAME, fileName);
            fortranFile.set(FortranFile.FOLDER_NAME, parsedFile.getParent());
        });

        // The JSON parsing assumes this node is also a statement, which is the reason for the name of the key
        if (attributes(fortranFile).has("leadingComments")) {
            var finalComments = attributes(fortranFile).getStringList("leadingComments");
            fortranFile.set(FortranFile.FINAL_COMMENTS, finalComments);
        } else {
            fortranFile.set(FortranFile.FINAL_COMMENTS, List.of());
        }
    }

    public void mainProgram(MainProgram mainProgram) {
        var programStmt = getStmtChildOptional(mainProgram, FlangName.PROGRAM_STMT);
        programStmt.ifPresent(mainProgram::addChild);

        var name = attributes().getOptionalString(mainProgram, "source", FlangName.PROGRAM_STMT, FlangName.NAME);
        // [specification-part]
        mainProgram.addChild(getChild(mainProgram, FlangName.SPECIFICATION_PART));
        // [execution-part]
        mainProgram.addChild(getChild(mainProgram, FlangName.EXECUTION_PART));
        // [internal-subprogram-part]
        if (attributes(mainProgram).has(FlangName.INTERNAL_SUBPROGRAM_PART)) {
            mainProgram.addChild(getChild(mainProgram, FlangName.INTERNAL_SUBPROGRAM_PART));
        }

        var endProgramStmt = getStmtChild(mainProgram, FlangName.END_PROGRAM_STMT);
        mainProgram.addChild(endProgramStmt);

        mainProgram.set(MainProgram.NAME, name);
    }

    public void specification(Specification specification) {

        if (attributes(specification).has(FlangName.STATEMENT)) {
            specification.addChildren(getChildren(specification, FlangName.STATEMENT));
        }

        if (attributes(specification).has(FlangName.DECLARATION_CONSTRUCT)) {
            specification.addChildren(getChildren(specification, FlangName.DECLARATION_CONSTRUCT));
        }


    }

    public void execution(Execution execution) {
        if (attributes(execution).has(FlangName.EXECUTION_PART_CONSTRUCT)) {
            execution.setChildren(getChildren(execution, FlangName.EXECUTION_PART_CONSTRUCT));
        }
    }

    public void internalSubprogram(InternalSubprogram internalSubprogram) {
        if (attributes(internalSubprogram).has(FlangName.CONTAINS_STMT.getStmtAttr())) {
            internalSubprogram.addChild(getChild(internalSubprogram, FlangName.CONTAINS_STMT.getStmtAttr()));
        }

        if (attributes(internalSubprogram).has(FlangName.INTERNAL_SUBPROGRAM)) {
            internalSubprogram.addChildren(getChildren(internalSubprogram, FlangName.INTERNAL_SUBPROGRAM));
        }
    }

    public void subroutine(Subroutine subroutine) {
        var subroutineStmt = getStmtChild(subroutine, FlangName.SUBROUTINE_STMT);
        subroutine.addChild(subroutineStmt);

        var specification = getChild(subroutine, FlangName.SPECIFICATION_PART);
        subroutine.addChild(specification);

        var execution = getChild(subroutine, FlangName.EXECUTION_PART);
        subroutine.addChild(execution);

        var endSubroutineStmt = getStmtChild(subroutine, FlangName.END_SUBROUTINE_STMT);
        subroutine.addChild(endSubroutineStmt);

        var name = attributes().getString(subroutine, "source", FlangName.SUBROUTINE_STMT, FlangName.NAME);
        subroutine.set(Subroutine.NAME, name);
    }
}
