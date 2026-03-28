package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.AFortranWeaverJoinPoint;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AJoinPoint;

public class GenericFortranJoinpoint extends AFortranWeaverJoinPoint {

    private final FortranNode node;

    public GenericFortranJoinpoint(FortranNode node) {
        this.node = node;
    }

    @Override
    public FortranNode getNode() {
        return node;
    }
}
