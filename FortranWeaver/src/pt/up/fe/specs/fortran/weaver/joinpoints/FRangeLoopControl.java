package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADataRef;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExpr;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ARangeLoopControl;

public class FRangeLoopControl extends ARangeLoopControl {

    private final RangeLoopControl rangeLoopControl;

    public FRangeLoopControl(RangeLoopControl rangeLoopControl) {
        super(new FLoopControl(rangeLoopControl));
        this.rangeLoopControl = rangeLoopControl;
    }

    @Override
    public AExpr getLowerImpl() {
        return FortranJoinpoints.create(rangeLoopControl.getLower(), AExpr.class);
    }

    @Override
    public AExpr getUpperImpl() {
        return FortranJoinpoints.create(rangeLoopControl.getUpper(), AExpr.class);
    }

    @Override
    public ADataRef getVarImpl() {
        return FortranJoinpoints.create(rangeLoopControl.getVar(), ADataRef.class);
    }

    @Override
    public AExpr getStepImpl() {
        return rangeLoopControl.getStep()
            .map(step -> FortranJoinpoints.create(step, AExpr.class))
            .orElse(null);
    }

    @Override
    public void setUpperImpl(AExpr upper) {
        rangeLoopControl.setUpper((Expr) upper.getNode());
    }

    @Override
    public void setStepImpl(AExpr step) {
        rangeLoopControl.setStep((Expr) step.getNode());
    }

    @Override
    public FortranNode getNode() {
        return rangeLoopControl;
    }
}
