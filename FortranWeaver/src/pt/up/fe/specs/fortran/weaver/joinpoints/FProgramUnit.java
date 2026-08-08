package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.ProgramUnit;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AProgramUnit;

public class FProgramUnit extends AProgramUnit {

    public final ProgramUnit programUnit;

    public FProgramUnit(ProgramUnit programUnit) {
        this.programUnit = programUnit;
    }

    @Override
    public FortranNode getNode() {
        return programUnit;
    }
}
