package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.type.shapes.ExplicitShapeSpecification;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class ShapesProcessor extends ANodeProcessor {
    public ShapesProcessor(FortranJsonResult data) {
        super(data);
    }

    public void explicitShapeSpec(ExplicitShapeSpecification explicitShapeSpec) {
        var bound = getChild(explicitShapeSpec, FlangName.SPECIFICATION_EXPR);
        explicitShapeSpec.addChild(bound);
    }
}
