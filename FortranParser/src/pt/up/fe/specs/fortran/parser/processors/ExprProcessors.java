package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.UnaryOperatorKind;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class ExprProcessors extends ANodeProcessor {


    public ExprProcessors(FortranJsonResult data) {
        super(data);
    }

    public void stringLiteral(StringLiteral stringLiteral) {
        stringLiteral.set(StringLiteral.SOURCE_LITERAL, attributes().getString(stringLiteral, "string"));
    }

    public void intLiteral(IntLiteral intLiteral) {
        intLiteral.set(StringLiteral.SOURCE_LITERAL, attributes().getString(intLiteral, "CharBlock"));

        if (attributes(intLiteral).has(FlangName.KIND_PARAM)) {
            intLiteral.setOptional(IntLiteral.KIND_PARAM, attributes().getString(intLiteral, "uint64_t", FlangName.KIND_PARAM));
        }
    }

    public void logicalLiteral(LogicalLiteral logicalLiteral) {
        logicalLiteral.set(StringLiteral.SOURCE_LITERAL, attributes().getString(logicalLiteral, "bool"));
    }

    public void realLiteral(RealLiteral realLiteral) {
        realLiteral.set(RealLiteral.SOURCE_LITERAL, attributes().get(attributes().getString(realLiteral, "real")).getString("source"));
    }

    public void parenExpr(ParenExpr parenExpr) {
        parenExpr.addChild(getChild(parenExpr, FlangName.EXPR));
    }

    public void unaryOperator(UnaryOperator unaryOperator) {
        unaryOperator.addChild(getChild(unaryOperator, FlangName.EXPR));

        String opName = attributes().getString(unaryOperator, "op");

        unaryOperator.set(UnaryOperator.OP, UnaryOperatorKind.valueOf(opName.toUpperCase()));
    }

    public void binaryOperator(BinaryOperator binaryOperator) {
        binaryOperator.addChild(getChild(binaryOperator, "left"));
        binaryOperator.addChild(getChild(binaryOperator, "right"));

        String opName = attributes().getString(binaryOperator, "op");

        binaryOperator.set(BinaryOperator.OP, BinaryOperatorKind.valueOf(opName.toUpperCase()));
    }

    public void arrayConstructor(ArrayConstructor arrayConstructor) {
        var acSpec = getChild(arrayConstructor, FlangName.AC_SPEC);
        arrayConstructor.addChild(acSpec);
    }

    public void acSpecification(AcSpecification acSpecification) {
        var acValueList = getChildren(acSpecification, "values");
        if (attributes(acSpecification).has("type")) {
            var type = getChild(acSpecification, "type");
            acSpecification.addChild(type);
        }
        acSpecification.addChildren(acValueList);
    }

    public void call(Call call) {
        call.addChild(getChild(call, FlangName.PROCEDURE_DESIGNATOR));

        if (attributes(call).has(FlangName.ACTUAL_ARG_SPEC))
            call.addChildren(getChildren(call, FlangName.ACTUAL_ARG_SPEC));
    }

    public void arraySubscriptExpr(ArraySubscriptExpr arraySubscriptExpr) {
        var base = getChild(arraySubscriptExpr, "base");
        arraySubscriptExpr.addChild(base);

        var subscriptIds = attributes(arraySubscriptExpr).getStringList("subscripts");
        var subscripts = subscriptIds.stream()
                .map(this::getSectionSubscript)
                .toList();

        arraySubscriptExpr.addChildren(subscripts);
    }

    public SectionSubscript getSectionSubscript(String id) {
        var attrs = attributes().get(id);

        if (attrs.has(FlangName.SUBSCRIPT_TRIPLET)) {
            return (SubscriptTriplet) getChild(id);
        }

        if (attrs.has(FlangName.EXPR)) {
            var subscript = factory().newNode(Subscript.class);
            var value = getChild(id);
            subscript.addChild(value);

            return subscript;
        }

        throw new RuntimeException("Section subscript with id '" + id + "' does not have a valid variant, expected either SubscriptTriplet or Subscript, but got attributes: " + attrs);
    }

    public void subscriptTriplet(SubscriptTriplet subscriptTriplet) {
        var attrs = attributes(subscriptTriplet);

        if (attrs.has("start")) {
            var start = getChild(subscriptTriplet, "start");
            subscriptTriplet.addChild(start);
            subscriptTriplet.set(SubscriptTriplet.HAS_START, true);
        }

        if (attrs.has("end")) {
            var end = getChild(subscriptTriplet, "end");
            subscriptTriplet.addChild(end);
            subscriptTriplet.set(SubscriptTriplet.HAS_END, true);
        }

        if (attrs.has("stride")) {
            var stride = getChild(subscriptTriplet, "stride");
            subscriptTriplet.addChild(stride);
            subscriptTriplet.set(SubscriptTriplet.HAS_STRIDE, true);
        }
    }

    public void acImpliedDo(AcImpliedDo acImpliedDo) {
        acImpliedDo.addChild(getChild(acImpliedDo, "AcImpliedDoControl"));
        acImpliedDo.addChildren(getChildren(acImpliedDo, "AcValue"));
    }

    public void acImpliedDoControl(AcImpliedDoControl control) {
        var rangeControl = getChild(control, "value");
        control.addChild(rangeControl);
    }

    public void argument(Argument argument) {
        argument.addChild(getChild(argument, FlangName.ACTUAL_ARG));
    }

    public void intComplexPart(IntComplexPart intComplexPart) {
        var intLiteral = getChild(intComplexPart, FlangName.SIGNED_INT_LITERAL_CONSTANT);
        intComplexPart.addChild(intLiteral);
    }

    public void realComplexPart(RealComplexPart realComplexPart) {
        var realLiteral = getChild(realComplexPart, FlangName.SIGNED_REAL_LITERAL_CONSTANT);
        realComplexPart.addChild(realLiteral);
    }

    public void namedComplexPart(NamedComplexPart namedComplexPart) {
        var namedLiteral = getChild(namedComplexPart, FlangName.NAMED_CONSTANT);
        namedComplexPart.addChild(namedLiteral);
    }

    public void complexLiteral(ComplexLiteral complexLiteral) {
        var real = getChild(complexLiteral, "real");
        complexLiteral.addChild(real);

        var imaginary = getChild(complexLiteral, "imaginary");
        complexLiteral.addChild(imaginary);
    }
}
