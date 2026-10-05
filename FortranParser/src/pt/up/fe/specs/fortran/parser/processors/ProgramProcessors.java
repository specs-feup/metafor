package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.Function;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.Subroutine;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.MainProgram;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.Module;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.ModuleSubprogramPart;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.SubprogramUnit;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
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
        var attrs = attributes(specification);

        var useStmts = getChildren(specification, FlangName.USE_STMT.getStmtAttr());
        specification.addChildren(useStmts);

        var importStmts = getChildren(specification, FlangName.IMPORT_STMT.getStmtAttr());
        specification.addChildren(importStmts);

        var implicitPartAttrs = attributes().get(attrs.getString(FlangName.IMPLICIT_PART));
        var implicitPartIds = implicitPartAttrs.getStringList(FlangName.IMPLICIT_PART_STMT);
        var implicitPartStmts = implicitPartIds.stream()
                .map(this::getChild)
                .map(this::toImplicitPartStmt)
                .toList();
        specification.addChildren(implicitPartStmts);

        var rawDeclConstructs = getChildren(specification, FlangName.DECLARATION_CONSTRUCT);
        var declConstructs = rawDeclConstructs.stream()
                .map(this::toDeclConstruct)
                .toList();
        specification.addChildren(declConstructs);
    }

    public void execBlock(ExecBlock execBlock) {
        var rawExecPartConstructs = getChildren(execBlock, FlangName.EXECUTION_PART_CONSTRUCT);

        var execPartConstructs = rawExecPartConstructs.stream()
                .map(this::toExecPartConstruct)
                .toList();

        execBlock.addChildren(execPartConstructs);
    }

    public void internalSubprogramPart(InternalSubprogramPart part) {
        var containsStmt = getStmtChild(part, FlangName.CONTAINS_STMT);
        part.addChild(containsStmt);

        if (attributes(part).has(FlangName.INTERNAL_SUBPROGRAM)) {
            var subprograms = getChildren(part, FlangName.INTERNAL_SUBPROGRAM);
            part.addChildren(subprograms);
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

    public void module(Module module) {
        var moduleStmt = getStmtChild(module, FlangName.MODULE_STMT);
        module.addChild(moduleStmt);

        var specification = getChild(module, FlangName.SPECIFICATION_PART);
        module.addChild(specification);

        var subprogramPart = getChildOptional(module, FlangName.MODULE_SUBPROGRAM_PART);
        subprogramPart.ifPresent(module::addChild);

        var endModuleStmt = getStmtChild(module, FlangName.END_MODULE_STMT);
        module.addChild(endModuleStmt);
    }

    public void moduleSubprogramPart(ModuleSubprogramPart part) {
        var containsStmt = getStmtChild(part, FlangName.CONTAINS_STMT);
        part.addChild(containsStmt);

        if (attributes(part).has(FlangName.MODULE_SUBPROGRAM)) {
            var subprograms = getChildren(part, FlangName.MODULE_SUBPROGRAM);
            part.addChildren(subprograms);
        }
    }
}
