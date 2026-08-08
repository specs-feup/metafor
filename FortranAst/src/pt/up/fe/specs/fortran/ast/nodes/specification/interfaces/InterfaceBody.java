package pt.up.fe.specs.fortran.ast.nodes.specification.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public abstract class InterfaceBody extends InterfaceSpecification {
    public InterfaceBody(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    protected Stmt getStartStmt() {
        return getChild(Stmt.class, 0);
    }

    public Specification getSpecification() {
        return getChild(Specification.class, 1);
    }

    protected Stmt getEndStmt() {
        return getChild(Stmt.class, 2);
    }

    @Override
    public String getCode() {
        var startStmtCode = getStartStmt().getCode();
        var specificationCode = getSpecification().getCode();
        var endStmtCode = getEndStmt().getCode();

        return startStmtCode + ln() + indent(specificationCode) + ln() + endStmtCode;
    }
}
