package pt.up.fe.specs.fortran.ast.nodes.specification.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.SubroutineStmt;

import java.util.Collection;

public class InterfaceSubroutine extends InterfaceBody {
    public InterfaceSubroutine(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return getSubroutineStmt().getSubroutineName();
    }

    public SubroutineStmt getSubroutineStmt() {
        return (SubroutineStmt) getStartStmt();
    }

    public SubroutineStmt getEndSubroutineStmt() {
        return (SubroutineStmt) getEndStmt();
    }
}
