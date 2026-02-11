package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;

public class StmtBlock extends FortranNode {

    public StmtBlock(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<Stmt> getStatements() {
        return getChildren(Stmt.class);
    }

    @Override
    public String getCode() {
        var code = new StringBuilder();
        for (var stmt : getStatements()) {
            code.append(stmt.getCode()).append(ln());
        }
        return code.toString();
    }
}
