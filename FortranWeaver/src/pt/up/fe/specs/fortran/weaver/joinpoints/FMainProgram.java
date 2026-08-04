package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.MainProgram;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AMainProgram;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ASpecification;

public class FMainProgram extends AMainProgram {
    public final MainProgram mainProgram;

    public FMainProgram(MainProgram mainProgram) {
        super(new FProgramUnit(mainProgram));
        this.mainProgram = mainProgram;
    }

    @Override
    public FortranNode getNode() {
        return mainProgram;
    }

    @Override
    public ASpecification getSpecificationImpl() {
        return FortranJoinpoints.create(mainProgram.getSpecification(), ASpecification.class);
    }
}
