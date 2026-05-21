package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.IfThenBlock;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AIfThenBlock;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AIfThenStatement;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AStatementBlock;

public class FIfThenBlock extends AIfThenBlock {

    public final IfThenBlock ifThenBlock;

    public FIfThenBlock(IfThenBlock ifThenBlock) {
        this.ifThenBlock = ifThenBlock;
    }

    @Override
    public AStatementBlock getBodyImpl() {
        return FortranJoinpoints.create(ifThenBlock.getBlock(), AStatementBlock.class);
    }

    @Override
    public AIfThenStatement getHeaderImpl() {
        return FortranJoinpoints.create(ifThenBlock.getIfThenStmt(), AIfThenStatement.class);
    }

    @Override
    public FortranNode getNode() {
        return ifThenBlock;
    }
}
