package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.ProgramUnit;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AProgramUnit;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ASpecification;

public class FProgramUnit extends AProgramUnit {

    public final ProgramUnit programUnit;

    public FProgramUnit(ProgramUnit programUnit) {
        this.programUnit = programUnit;
    }

    @Override
    public ASpecification getSpecificationImpl() {
        return FortranJoinpoints.create(programUnit.getSpecification(), ASpecification.class);
    }

    @Override
    public FortranNode getNode() {
        return programUnit;
    }
}
