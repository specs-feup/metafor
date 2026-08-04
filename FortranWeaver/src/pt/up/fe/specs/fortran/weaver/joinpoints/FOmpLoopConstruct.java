package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoConstruct;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADoStatement;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpLoopConstruct;

public class FOmpLoopConstruct extends AOmpLoopConstruct {

    public final OmpLoopConstruct ompLoopConstruct;

    public FOmpLoopConstruct(OmpLoopConstruct ompLoopConstruct) {
        super(new FOmpConstruct(ompLoopConstruct));
        this.ompLoopConstruct = ompLoopConstruct;
    }

    @Override
    public void setLoopImpl(ADoStatement loop) {
        ompLoopConstruct.setLoop((DoConstruct) loop.getNode());
    }

    @Override
    public FortranNode getNode() {
        return ompLoopConstruct;
    }
}
