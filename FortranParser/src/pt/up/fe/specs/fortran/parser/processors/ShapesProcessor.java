package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.type.shapes.*;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class ShapesProcessor extends ANodeProcessor {
    public ShapesProcessor(FortranJsonResult data) {
        super(data);
    }

    public void explicitShapeSpec(ExplicitShapeSpecification explicitShapeSpec) {
        boundedShapeSpec(explicitShapeSpec);
    }

    public void allocateShapeSpec(AllocateShapeSpecification allocateShapeSpec) {
        boundedShapeSpec(allocateShapeSpec);
    }

    public void boundedShapeSpec(BoundedShapeSpecification boundedShapeSpec) {
        if (attributes(boundedShapeSpec).has("lower_bound")) {
            var lower_bound = getChild(boundedShapeSpec, "lower_bound");
            boundedShapeSpec.addChild(lower_bound);
        }
        var upper_bound = getChild(boundedShapeSpec, "upper_bound");
        boundedShapeSpec.addChild(upper_bound);
    }

    public void deferredShapeSpecLis(DeferredShapeSpecList deferredShapeSpecList) {
        var numberOfColons = attributes(deferredShapeSpecList).getString("int");
        deferredShapeSpecList.set(DeferredShapeSpecList.RANK, Integer.parseInt(numberOfColons));
    }

    public void assumedImpliedShapeSpec(AssumedImpliedShapeSpec assumedImpliedShapeSpec) {

    }
}
