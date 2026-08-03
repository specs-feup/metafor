package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.InternalPart;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.Optional;

public abstract class Subprogram extends FortranNode {
    public final static DataKey<String> NAME = KeyFactory.string("name");

    public Subprogram(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    protected Stmt getStartStmt() {
        return getChild(Stmt.class, 0);
    }

    protected Stmt getEndStmt() {
        return getChild(Stmt.class, getNumChildren() - 1);
    }

    public Specification getSpecification() {
        return getChild(Specification.class, 1);
    }

    public Execution getExecution() {
        return getChild(Execution.class);
    }

    public Optional<InternalPart> getInternalPart() {
        return getChildOf(InternalPart.class);
    }

    public String getCode() {
        var startStmtCode = getStartStmt().getCode();
        var specificationCode = getSpecification().getCode() + ln();
        var executionCode = getExecution().getCode();
        var internalPartCode = getInternalPart()
                .map(part -> ln() + part.getCode())
                .orElse("");
        var endStmtCode = getEndStmt().getCode();

        return startStmtCode + ln()
                + indent(specificationCode + executionCode + internalPartCode) + ln()
                + endStmtCode;
    }
}
