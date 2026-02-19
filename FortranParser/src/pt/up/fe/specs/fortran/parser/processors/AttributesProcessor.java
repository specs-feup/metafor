package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.type.attributes.ArraySpecifier;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class AttributesProcessor extends ANodeProcessor {
    public AttributesProcessor(FortranJsonResult data) {
        super(data);
    }

    public void arraySpecifier(ArraySpecifier arraySpecifier) {
        var shapes = getChildren(arraySpecifier, "value");
        arraySpecifier.addChildren(shapes);
    }
}
