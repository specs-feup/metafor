package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExecutableStatement;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExecution;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AStatement;

import java.util.stream.Collectors;

public class FExecution extends AExecution {

    private final Execution execution;

    public FExecution(Execution execution) {
        super(new FStatementBlock(execution));
        this.execution = execution;
    }

    @Override
    public AExecutableStatement[] getExecutableStmtsArrayImpl() {
        return execution.getExecutableStatements()
                .stream()
                .map(FortranJoinpoints::create)
                .toList()
                .toArray(new AExecutableStatement[0]);
    }

    @Override
    public void insertBeginImpl(AExecutableStatement stmt) {
        execution.addChild(0, stmt.getNode());
    }

    @Override
    public void insertEndImpl(AExecutableStatement stmt) {
        execution.addChild(stmt.getNode());
    }

    @Override
    public FortranNode getNode() {
        return execution;
    }
}
