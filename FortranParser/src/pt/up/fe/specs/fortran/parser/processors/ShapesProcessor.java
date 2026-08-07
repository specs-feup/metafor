package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.specification.shape.AssumedImpliedShape;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.DeferredShapeArraySpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ExplicitShape;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class ShapesProcessor extends ANodeProcessor {
    public ShapesProcessor(FortranJsonResult data) {
        super(data);
    }

    public void explicitShapeSpec(ExplicitShape explicitShapeSpec) {
        if (attributes(explicitShapeSpec).has("lower_bound")) {
            var lower_bound = getChild(explicitShapeSpec, "lower_bound");
            explicitShapeSpec.addChild(lower_bound);
        }

        var upper_bound = getChild(explicitShapeSpec, "upper_bound");
        explicitShapeSpec.addChild(upper_bound);
    }

    public void deferredShapeSpecLis(DeferredShapeArraySpec deferredShapeSpecList) {
        var numberOfColons = attributes(deferredShapeSpecList).getString("int");
        deferredShapeSpecList.set(DeferredShapeArraySpec.RANK, Integer.parseInt(numberOfColons));
    }

    public void assumedImpliedShapeSpec(AssumedImpliedShape assumedImpliedShapeSpec) {

    }
}
