package pt.up.fe.specs.fortran.ast.nodes.stmt.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.GenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.Optional;

public abstract class InterfaceStmt extends Stmt {
    public InterfaceStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public abstract Optional<GenericSpec> getGenericSpec();
}
