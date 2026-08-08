package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.InternalSubprogramPart;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;

import java.util.Collection;
import java.util.Optional;

public class MainProgram extends ProgramUnit {
    public MainProgram(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getName() {
        return getProgramStmt().map(ProgramStmt::getProgramName);
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

    public Optional<InternalSubprogramPart> getInternalPart() {
        return getChildOf(InternalSubprogramPart.class);
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
