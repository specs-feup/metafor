package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;

public class CommonStmt extends SpecificationStmt {
    public CommonStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<CommonBlock> getBlocks() {
        return getChildren(CommonBlock.class);
    }
}
