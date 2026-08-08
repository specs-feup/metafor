package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.specification.shape.AssumedImpliedShape;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.AssumedShape;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.DeferredShapeSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ExplicitShape;
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

    public void assumedImpliedShapeSpec(AssumedImpliedShape assumedImpliedShapeSpec) {

    }

    public void deferredShapeSpecList(DeferredShapeSpec deferredShapeSpecList) {
        var numberOfColons = attributes(deferredShapeSpecList).getString("int");
        deferredShapeSpecList.set(DeferredShapeSpec.RANK, Integer.parseInt(numberOfColons));
    }
}
