package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class DeclStmt extends Stmt {
    public DeclStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
