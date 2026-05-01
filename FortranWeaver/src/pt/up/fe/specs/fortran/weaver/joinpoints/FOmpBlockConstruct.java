package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExecution;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpBlockConstruct;

public class FOmpBlockConstruct extends AOmpBlockConstruct {

    public final OmpBlockConstruct ompBlockConstruct;

    public FOmpBlockConstruct(OmpBlockConstruct ompBlockConstruct) {
        super(new FOmpConstruct(ompBlockConstruct));
        this.ompBlockConstruct = ompBlockConstruct;
    }

    @Override
    public void setBodyImpl(AExecution body) {
        ompBlockConstruct.setBody((Execution) body.getNode());
    }

    @Override
    public FortranNode getNode() {
        return ompBlockConstruct;
    }
}
