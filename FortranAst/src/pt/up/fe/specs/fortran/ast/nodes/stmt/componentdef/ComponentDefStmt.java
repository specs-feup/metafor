package pt.up.fe.specs.fortran.ast.nodes.stmt.componentdef;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public abstract class ComponentDefStmt extends Stmt {
    public ComponentDefStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
