package pt.up.fe.specs.fortran.ast.nodes.stmt.typedef;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndTypeStmt extends Stmt {
    public EndTypeStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
