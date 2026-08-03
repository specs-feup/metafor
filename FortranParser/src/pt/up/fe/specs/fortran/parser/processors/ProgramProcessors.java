package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;
import pt.up.fe.specs.util.SpecsIo;

public class ProgramProcessors extends ANodeProcessor {


    public ProgramProcessors(FortranJsonResult data) {
        super(data);
    }

    public void program(FortranFile fortranFile) {
        fortranFile.setChildren(getChildren(fortranFile, FlangName.PROGRAM_UNIT));

        var lastParsedFile = fortranFile.get(FortranNode.CONTEXT).get(FortranContext.LAST_PARSED_FILE).orElse(null);
        if (lastParsedFile != null) {
            var fileName = lastParsedFile.getName();
            if (fileName.endsWith(".json")) {
                fileName = SpecsIo.removeExtension(fileName) + ".f90";
            }

            fortranFile.set(FortranFile.FILE_NAME, fileName);
            fortranFile.set(FortranFile.FOLDER_NAME, lastParsedFile.getParent());
        }
    }

    public void mainProgram(MainProgram mainProgram) {

        var name = attributes().getOptionalString(mainProgram, "source", FlangName.PROGRAM_STMT, FlangName.NAME);
        // [specification-part]
        mainProgram.addChild(getChild(mainProgram, FlangName.SPECIFICATION_PART));
        // [execution-part]
        mainProgram.addChild(getChild(mainProgram, FlangName.EXECUTION_PART));
        // [internal-subprogram-part]
        if (attributes(mainProgram).has(FlangName.INTERNAL_SUBPROGRAM_PART)) {
            mainProgram.addChild(getChild(mainProgram, FlangName.INTERNAL_SUBPROGRAM_PART));
        }

        mainProgram.set(MainProgram.PROGRAM_NAME, name);
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
        var name = attributes().getString(subroutine, "source", FlangName.SUBROUTINE_STMT, FlangName.NAME);
        // [specification-part]
        subroutine.addChild(getChild(subroutine, FlangName.SPECIFICATION_PART));
        // [execution-part]
        subroutine.addChild(getChild(subroutine, FlangName.EXECUTION_PART));
        // [internal-subprogram-part]

        subroutine.set(Subroutine.SUBROUTINE_NAME, name);

        String statementId = attributes().get(attributes(subroutine).getString(FlangName.SUBROUTINE_STMT.getStmtAttr())).getString("statement");

        if (attributes().has(statementId, FlangName.DUMMY_ARG)) {
            subroutine.addChildren(getChildren(statementId, FlangName.DUMMY_ARG));
        }
    }

    public void function(Function function) {
        var name = attributes().getString(function, "source", FlangName.FUNCTION_STMT, FlangName.NAME);
        // [specification-part]
        function.addChild(getChild(function, FlangName.SPECIFICATION_PART));
        // [execution-part]
        function.addChild(getChild(function, FlangName.EXECUTION_PART));
        // [internal-subprogram-part]

        function.set(Function.FUNCTION_NAME, name);

        String statementId = attributes().get(attributes(function).getString(FlangName.FUNCTION_STMT.getStmtAttr())).getString("statement");
        function.addChildren(getChildren(statementId, FlangName.FUNCTION_ARGUMENT_DECL));
    }
}
