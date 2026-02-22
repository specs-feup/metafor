package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.type.shapes.DeferredShapeSpecList;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.ExplicitShapeSpecification;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class ShapesProcessor extends ANodeProcessor {
    public ShapesProcessor(FortranJsonResult data) {
        super(data);
    }

    public void explicitShapeSpec(ExplicitShapeSpecification explicitShapeSpec) {
        if (attributes(explicitShapeSpec).has("lower_bound")) {
            var lower_bound = getChild(explicitShapeSpec, "lower_bound");
            explicitShapeSpec.addChild(lower_bound);
        }
        var upper_bound = getChild(explicitShapeSpec, "upper_bound");
        explicitShapeSpec.addChild(upper_bound);
    }

    public void deferredShapeSpecLis(DeferredShapeSpecList deferredShapeSpecList) {
        var numberOfColons = attributes(deferredShapeSpecList).getString("int");
        deferredShapeSpecList.set(DeferredShapeSpecList.RANK, Integer.parseInt(numberOfColons));
    }
}
