package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.LiteralNode;

import java.util.Collection;

public class LiteralExecutionStmt extends ExecutableStmt implements LiteralNode {

    public LiteralExecutionStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return getLiteralCode();
    }
}
