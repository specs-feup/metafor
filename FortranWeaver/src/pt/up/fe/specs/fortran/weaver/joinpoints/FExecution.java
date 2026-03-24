package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExecutableStatement;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExecution;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AStatement;

public class FExecution extends AExecution {

    private final Execution execution;

    public FExecution(Execution execution) {
        super(new FStatementBlock(execution));
        this.execution = execution;
    }

    @Override
    public AExecutableStatement[] getExecutableStmtsArrayImpl() {
        return (AExecutableStatement[]) execution.getExecutableStatements()
                .stream()
                .map(FortranJoinpoints::create)
                .toArray();
    }

    @Override
    public FortranNode getNode() {
        return execution;
    }
}
