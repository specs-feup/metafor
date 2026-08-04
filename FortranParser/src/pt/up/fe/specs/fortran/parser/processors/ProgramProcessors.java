package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.InternalSubprogramPart;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.Function;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.Subroutine;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.MainProgram;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.SubprogramUnit;
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

        // The JSON parsing assumes this node is also a statement, which is the reason for the name of the key
        if (attributes(fortranFile).has("leadingComments")) {
            var finalComments = attributes(fortranFile).getStringList("leadingComments");
            fortranFile.set(FortranFile.FINAL_COMMENTS, finalComments);
        } else {
            fortranFile.set(FortranFile.FINAL_COMMENTS, List.of());
        }
    }

    public void addSubprogramBody(FortranNode node) {
        var specification = getChild(node, FlangName.SPECIFICATION_PART);
        node.addChild(specification);

        var execution = getChild(node, FlangName.EXECUTION_PART);
        node.addChild(execution);

        var internalPart = getChildOptional(node, FlangName.INTERNAL_SUBPROGRAM_PART);
        internalPart.ifPresent(node::addChild);
    }

    public void mainProgram(MainProgram mainProgram) {
        var programStmt = getStmtChildOptional(mainProgram, FlangName.PROGRAM_STMT);
        programStmt.ifPresent(mainProgram::addChild);

        addSubprogramBody(mainProgram);

        var endProgramStmt = getStmtChild(mainProgram, FlangName.END_PROGRAM_STMT);
        mainProgram.addChild(endProgramStmt);
    }

    public void subprogramUnit(SubprogramUnit subprogramUnit) {
        var variant = attributes(subprogramUnit).getVariantName();
        if (variant != FlangName.SUBROUTINE_SUBPROGRAM) {
            throw new RuntimeException("Unknown variant: " + variant);
        }

        var child = getChild(subprogramUnit, variant);
        subprogramUnit.addChild(child);
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

    public void internalSubprogramPart(InternalSubprogramPart part) {
        if (attributes(part).has(FlangName.CONTAINS_STMT.getStmtAttr())) {
            part.addChild(getChild(part, FlangName.CONTAINS_STMT.getStmtAttr()));
        }

        if (attributes(part).has(FlangName.INTERNAL_SUBPROGRAM)) {
            part.addChildren(getChildren(part, FlangName.INTERNAL_SUBPROGRAM));
        }
    }

    public void subroutine(Subroutine subroutine) {
        var subroutineStmt = getStmtChild(subroutine, FlangName.SUBROUTINE_STMT);
        subroutine.addChild(subroutineStmt);

        addSubprogramBody(subroutine);

        var endSubroutineStmt = getStmtChild(subroutine, FlangName.END_SUBROUTINE_STMT);
        subroutine.addChild(endSubroutineStmt);
    }

    public void function(Function function) {
        var functionStmt = getStmtChild(function, FlangName.FUNCTION_STMT);
        function.addChild(functionStmt);

        addSubprogramBody(function);

        var endFunctionStmt = getStmtChild(function, FlangName.END_FUNCTION_STMT);
        function.addChild(endFunctionStmt);
    }
}
