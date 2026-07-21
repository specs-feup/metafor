package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableConstruct;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExecutableConstruct;

public class FExecutableConstruct extends AExecutableConstruct {
    public final ExecutableConstruct executableConstruct;

    public FExecutableConstruct(ExecutableConstruct executableConstruct) {
        this.executableConstruct = executableConstruct;
    }

    @Override
    public FortranNode getNode() {
        return executableConstruct;
    }
}
