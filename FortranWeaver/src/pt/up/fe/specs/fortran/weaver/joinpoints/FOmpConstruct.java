package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpConstruct;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpConstruct;

public class FOmpConstruct extends AOmpConstruct {

    public final OmpConstruct ompConstruct;

    public FOmpConstruct(OmpConstruct ompConstruct) {
        super(new FExecutableStatement(ompConstruct));
        this.ompConstruct = ompConstruct;
    }

    @Override
    public FortranNode getNode() {
        return ompConstruct;
    }
}
