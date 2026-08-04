package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.AttributeSpecifier;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AAttributeSpecifier;

public class FAttributeSpecifier extends AAttributeSpecifier {

    public final AttributeSpecifier attributeSpecifier;

    public FAttributeSpecifier(AttributeSpecifier attributeSpecifier) {
        this.attributeSpecifier = attributeSpecifier;
    }

    @Override
    public FortranNode getNode() {
        return attributeSpecifier;
    }
}
