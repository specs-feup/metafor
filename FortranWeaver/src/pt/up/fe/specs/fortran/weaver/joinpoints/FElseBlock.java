package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.ElseBlock;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AElseBlock;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AStatementBlock;

public class FElseBlock extends AElseBlock {

    public final ElseBlock elseBlock;

    public FElseBlock(ElseBlock elseBlock) {
        this.elseBlock = elseBlock;
    }

    @Override
    public AStatementBlock getBodyImpl() {
        return FortranJoinpoints.create(elseBlock.getBlock(), AStatementBlock.class);
    }

    @Override
    public FortranNode getNode() {
        return elseBlock;
    }
}
