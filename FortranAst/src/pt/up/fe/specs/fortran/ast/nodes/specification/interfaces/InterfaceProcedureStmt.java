package pt.up.fe.specs.fortran.ast.nodes.specification.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

// TODO(Process-ing): Implement this node
public class InterfaceProcedureStmt extends InterfaceSpecification {
    public InterfaceProcedureStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
