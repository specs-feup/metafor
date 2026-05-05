package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Initialization;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AInitialization;

public class FInitialization extends AInitialization {

    public final Initialization initialization;

    public FInitialization(Initialization initialization) {
        this.initialization = initialization;
    }

    @Override
    public FortranNode getNode() {
        return null;
    }
}
