package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADataRef;

public class FDataRef extends ADataRef {

    private final DataRef dataRef;

    public FDataRef(DataRef dataRef) {
        super(new FDesignator(dataRef));
        this.dataRef = dataRef;
    }

    @Override
    public String getNameImpl() {
        return dataRef.getName();
    }

    @Override
    public FortranNode getNode() {
        return dataRef;
    }
}
