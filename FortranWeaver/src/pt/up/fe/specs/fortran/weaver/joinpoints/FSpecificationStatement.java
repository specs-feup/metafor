package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.SpecificationStmt;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ASpecificationStatement;

public class FSpecificationStatement extends ASpecificationStatement {

    public final SpecificationStmt specificationStmt;

    public FSpecificationStatement(SpecificationStmt specificationStmt) {
        super(new FStatement(specificationStmt));
        this.specificationStmt = specificationStmt;
    }

    @Override
    public FortranNode getNode() {
        return specificationStmt;
    }
}
