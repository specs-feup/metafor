package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.InternalPart;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.EndProgramStmt;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.ProgramStmt;

import java.util.Collection;
import java.util.Optional;

public class MainProgram extends ProgramUnit {
    public final static DataKey<Optional<String>> NAME = KeyFactory.optional("name");

    public MainProgram(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getName() {
        return get(NAME);
    }

    public Optional<ProgramStmt> getProgramStmt() {
        return getChildTry(ProgramStmt.class, 0);
    }

    public Specification getSpecification() {
        return getChild(Specification.class);
    }

    public Execution getExecution() {
        return getChild(Execution.class);
    }

    public Optional<InternalPart> getInternalPart() {
        return getChildOf(InternalPart.class);
    }

    public EndProgramStmt getEndProgramStmt() {
        return getChild(EndProgramStmt.class);
    }

    @Override
    public String getCode() {
        var programStmtCode = getProgramStmt()
                .map(stmt -> stmt.getCode() + ln())
                .orElse("");
        var specificationCode = getSpecification().getCode() + ln();
        var executionCode = getExecution().getCode();
        var internalPartCode = getInternalPart()
                .map(part -> ln() + part.getCode())
                .orElse("");
        var endProgramStmtCode = getEndProgramStmt().getCode();

        return programStmtCode
                + indent(specificationCode + executionCode + internalPartCode) + ln()
                + endProgramStmtCode;
    }
}
