package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.Subprogram;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ASpecification;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ASubprogram;

public class FSubprogram extends ASubprogram {
    private final Subprogram subprogram;

    public FSubprogram(Subprogram subprogram) {
        this.subprogram = subprogram;
    }

    @Override
    public FortranNode getNode() {
        return subprogram;
    }

    @Override
    public String getModuleNameImpl() {
        return subprogram.getName();
    }

    @Override
    public ASpecification getSpecificationImpl() {
        return FortranJoinpoints.create(subprogram.getSpecification(), ASpecification.class);
    }
}
