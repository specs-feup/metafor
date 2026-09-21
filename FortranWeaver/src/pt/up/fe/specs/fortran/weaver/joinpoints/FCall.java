package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Call;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ACall;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADataRef;

public class FCall extends ACall {

    private final Call call;

    public FCall(Call call) {
        super(new FExpr(call));
        this.call = call;
    }

    @Override
    public ADataRef getCalleeImpl() {
        return FortranJoinpoints.create(call.getCallee(), ADataRef.class);
    }

    @Override
    public FortranNode getNode() {
        return null;
    }
}
