package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.UseStmt;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ASpecification;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AUseStatement;

public class FSpecification extends ASpecification {

    public final Specification specification;

    public FSpecification(Specification specification) {
        super(new FStatementBlock(specification));
        this.specification = specification;
    }

    @Override
    public void addUseStmtImpl(AUseStatement stmt) {
        specification.addUseStmt((UseStmt) stmt.getNode());
    }

    @Override
    public FortranNode getNode() {
        return null;
    }
}
