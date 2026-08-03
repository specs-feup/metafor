package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Subroutine extends InternalSubprogram {
    public Subroutine(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public SubroutineStmt getSubroutineStmt() {
        return (SubroutineStmt) getStartStmt();
    }

    public EndSubroutineStmt getEndSubroutineStmt() {
        return (EndSubroutineStmt) getEndStmt();
    }
}
