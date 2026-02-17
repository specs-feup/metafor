package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;

import java.util.Collection;

public class ElseBlock extends StmtBlock {
    public ElseBlock(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public StmtBlock getBlock() {
        return getChild(StmtBlock.class, 0);
    }

    @Override
    public String getCode() {
        var code = new StringBuilder();

        var block = getBlock();

        code.append(keyword(FortranKeyword.ELSE)).append(ln());

        code.append(block.getCode());

        return code.toString();
    }
}
