package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Designator;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADesignator;

public class FDesignator extends ADesignator {

    private final Designator designator;

    public FDesignator(Designator designator) {
        super(new FExpr(designator));
        this.designator = designator;
    }

    @Override
    public FortranNode getNode() {
        return designator;
    }
}
