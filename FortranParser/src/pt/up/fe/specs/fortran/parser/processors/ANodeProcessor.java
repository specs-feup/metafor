package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.construct.DeclConstruct;
import pt.up.fe.specs.fortran.ast.nodes.program.construct.ExecPartConstruct;
import pt.up.fe.specs.fortran.ast.nodes.program.construct.SpecConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.implicit.ImplicitPartStmt;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class ANodeProcessor implements NodeProcessor {


    private final FortranJsonResult data;

    public ANodeProcessor(FortranJsonResult data) {
        this.data = data;
    }

    @Override
    public FortranJsonResult data() {
        return data;
    }

    // Utility functions

    public DeclConstruct toDeclConstruct(FortranNode node) {
        if (node instanceof DeclConstruct declConstruct) {
            return declConstruct;
        }

        if (node instanceof DeclStmt declStmt) {
            return factory().declStmtAdapter(declStmt);
        }

        if (node instanceof IDEStmt ideStmt) {
            return factory().ideStmtDeclAdapter(ideStmt);
        }

        if (node instanceof SpecStmt || node instanceof ISStmt || node instanceof CompilerDirective) {
            return toSpecConstruct(node);
        }

        throw new RuntimeException("Cannot convert node to DeclConstruct: " + node);
    }

    public SpecConstruct toSpecConstruct(FortranNode node) {
        if (node instanceof SpecConstruct specConstruct) {
            return specConstruct;
        }

        if (node instanceof SpecStmt specStmt) {
            return factory().specStmtAdapter(specStmt);
        }

        if (node instanceof CompilerDirective compilerDirective) {
            return factory().directiveSpecAdapter(compilerDirective);
        }

        if (node instanceof ISStmt isStmt) {
            return factory().isStmtSpecAdapter(isStmt);
        }

        throw new RuntimeException("Cannot convert node to SpecConstruct: " + node);
    }

    public ExecPartConstruct toExecPartConstruct(FortranNode node) {
        if (node instanceof ExecPartConstruct execPartConstruct) {
            return execPartConstruct;
        }

        if (node instanceof ActionStmt actionStmt) {
            return factory().actionStmtAdapter(actionStmt);
        }

        if (node instanceof IDEStmt ideStmt) {
            return factory().ideStmtExecAdapter(ideStmt);
        }

        if (node instanceof CompilerDirective compilerDirective) {
            return factory().directiveExecAdapter(compilerDirective);
        }

        throw new RuntimeException("Cannot convert node to ExecPartConstruct: " + node);
    }

    public ImplicitPartStmt toImplicitPartStmt(FortranNode node) {
        if (node instanceof ImplicitPartStmt implicitPartStmt) {
            return implicitPartStmt;
        }

        if (node instanceof IDEStmt ideStmt) {
            return factory().ideStmtImplicitAdapter(ideStmt);
        }

        if (node instanceof ISStmt isStmt) {
            return factory().isStmtImplicitAdapter(isStmt);
        }

        throw new RuntimeException("Cannot convert node to ImplicitPartStmt: " + node);
    }
}
