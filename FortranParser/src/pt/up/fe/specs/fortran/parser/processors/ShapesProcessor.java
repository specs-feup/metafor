package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.specification.shape.*;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class ShapesProcessor extends ANodeProcessor {
    public ShapesProcessor(FortranJsonResult data) {
        super(data);
    }

    public void explicitShape(ExplicitShape explicitShape) {
        // TODO(Process-ing): Convert the JSON properties to the camel case standard
        if (attributes(explicitShape).has("lower_bound")) {
            var lowerBound = getChild(explicitShape, "lower_bound");
            explicitShape.addChild(lowerBound);
        }

        var upperBound = getChild(explicitShape, "upper_bound");
        explicitShape.addChild(upperBound);
    }

    public void assumedShape(AssumedShape assumedShape) {
        var lowerBound = getChildOptional(assumedShape, FlangName.EXPR);
        lowerBound.ifPresent(assumedShape::addChild);
    }

    public void assumedImpliedShape(AssumedImpliedShape assumedImpliedShape) {
        var lowerBound = getChildOptional(assumedImpliedShape, FlangName.EXPR);
        lowerBound.ifPresent(assumedImpliedShape::addChild);
    }

    public void explicitShapeArraySpec(ExplicitShapeArraySpec arraySpec) {
        var explicitShapes = getChildren(arraySpec, FlangName.EXPLICIT_SHAPE_SPEC);
        arraySpec.addChildren(explicitShapes);
    }

    public void assumedShapeArraySpec(AssumedShapeArraySpec arraySpec) {
        var assumedShapes = getChildren(arraySpec, FlangName.ASSUMED_SHAPE_SPEC);
        arraySpec.addChildren(assumedShapes);
    }

    public void deferredShapeSpec(DeferredShapeSpec deferredShapeSpec) {
        var numberOfColons = attributes(deferredShapeSpec).getString("int");
        deferredShapeSpec.set(DeferredShapeSpec.RANK, Integer.parseInt(numberOfColons));
    }

    public void assumedSizeSpec(AssumedSizeSpec assumedSizeSpec) {
        var explicitShapes = getChildren(assumedSizeSpec, FlangName.EXPLICIT_SHAPE_SPEC);
        assumedSizeSpec.addChildren(explicitShapes);

        var assumedImpliedShape = getChild(assumedSizeSpec, FlangName.ASSUMED_IMPLIED_SPEC);
        assumedSizeSpec.addChild(assumedImpliedShape);
    }

    public void impliedShapeSpec(ImpliedShapeSpec impliedShapeSpec) {
        var assumedImpliedShapes = getChildren(impliedShapeSpec, FlangName.ASSUMED_IMPLIED_SPEC);
        impliedShapeSpec.addChildren(assumedImpliedShapes);
    }

    public void assumedRankSpec(AssumedRankSpec ignoredSpec) {}
}
