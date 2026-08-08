package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.UseStmt;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AUseStatement;

public class FUseStatement extends AUseStatement {

    public final UseStmt useStmt;

    public FUseStatement(UseStmt useStmt) {
        super(new FStatement(useStmt));
        this.useStmt = useStmt;
    }

    @Override
    public FortranNode getNode() {
        return useStmt;
    }

    @Override
    public String getModuleNameImpl() {
        return useStmt.getName();
    }
}
