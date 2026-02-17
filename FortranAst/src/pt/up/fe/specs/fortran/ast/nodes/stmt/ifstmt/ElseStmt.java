package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;

import java.util.Collection;

public class ElseStmt extends StmtBlock {
    public ElseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
