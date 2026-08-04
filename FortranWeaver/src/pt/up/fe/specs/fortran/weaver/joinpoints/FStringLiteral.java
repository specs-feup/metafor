package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.StringLiteral;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AStringLiteral;

public class FStringLiteral extends AStringLiteral {

    private final StringLiteral stringLiteral;

    public FStringLiteral(StringLiteral stringLiteral) {
        super(new FKindedLiteral(stringLiteral));
        this.stringLiteral = stringLiteral;
    }

    @Override
    public FortranNode getNode() {
        return stringLiteral;
    }
}
