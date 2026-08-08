package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.Subroutine;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ASubroutine;

public class FSubroutine extends ASubroutine {
    public final Subroutine subroutine;

    public FSubroutine(Subroutine subroutine) {
        super(new FInternalSubprogram(subroutine));

        this.subroutine = subroutine;
    }

    @Override
    public FortranNode getNode() {
        return subroutine;
    }
}
