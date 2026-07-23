package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.UnaryOperatorKind;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.Optional;

public class ExprProcessors extends ANodeProcessor {


    public ExprProcessors(FortranJsonResult data) {
        super(data);
    }

    public Optional<String> getKindParam(String kindParamId) {
        var kindParamAttrs = attributes().get(kindParamId);

        switch (kindParamAttrs.getVariantKey()) {
            case "uint64_t" -> {
                var kindParam = kindParamAttrs.getString("uint64_t");
                return Optional.of(kindParam);
            }
            case "Expr" -> {
                var nameAttrs = attributes().get(kindParamAttrs.getString(FlangName.EXPR));
                var kindParam = nameAttrs.getString("source");
                return Optional.of(kindParam);
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    public void stringLiteral(StringLiteral stringLiteral) {
        var contents = attributes().getString(stringLiteral, "string");
        stringLiteral.set(StringLiteral.CONTENTS, contents);

        var kindParam = attributes(stringLiteral)
                .getOptionalString(FlangName.KIND_PARAM)
                .flatMap(this::getKindParam);
        stringLiteral.set(StringLiteral.KIND_PARAM, kindParam);
    }

    public void intLiteral(IntLiteral intLiteral) {
        var source = attributes().getString(intLiteral, "CharBlock");
        intLiteral.set(IntLiteral.SOURCE, source);

        var kindParam = attributes(intLiteral)
                .getOptionalString(FlangName.KIND_PARAM)
                .flatMap(this::getKindParam);
        intLiteral.set(IntLiteral.KIND_PARAM, kindParam);
    }

    public void logicalLiteral(LogicalLiteral logicalLiteral) {
        var value = attributes().getString(logicalLiteral, "bool").equals("1");
        logicalLiteral.set(LogicalLiteral.VALUE, value);

        var kindParam = attributes(logicalLiteral)
                .getOptionalString(FlangName.KIND_PARAM)
                .flatMap(this::getKindParam);
        logicalLiteral.set(LogicalLiteral.KIND_PARAM, kindParam);
    }

    public void realLiteral(RealLiteral realLiteral) {
        var attrs = attributes(realLiteral);
        var sign = "";

        if (attrs.has(FlangName.REAL_LITERAL_CONSTANT)) {
            sign = attrs.getOptionalString(FlangName.SIGN).orElseGet(() -> "");

            var childId = attrs.getString(FlangName.REAL_LITERAL_CONSTANT);
            attrs = attributes().get(childId);
        }

        var realId = attrs.getString("real");
        var realSource = attributes().get(realId).getString("source");
        realLiteral.set(RealLiteral.SOURCE, sign + realSource);

        var kindParam = attrs.getOptionalString("kind")
                .flatMap(this::getKindParam);

        realLiteral.set(RealLiteral.KIND_PARAM, kindParam);
    }

    public void namedLiteral(NamedLiteral namedLiteral) {
        var name = attributes().getString(namedLiteral, "source", FlangName.NAME);
        namedLiteral.set(NamedLiteral.NAME, name);
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

        var opName = attributes().getString(binaryOperator, "op");
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

    public void substring(Substring substring) {
        var ref = getChild(substring, FlangName.DATA_REF);
        substring.addChild(ref);

        var lowerId = attributes().getOptionalString(substring, "lower", FlangName.SUBSTRING_RANGE);
        var lowerExpr = lowerId.map(this::getChild);
        lowerExpr.ifPresent(lower -> {
            substring.addChild(lower);
            substring.setOptional(Substring.LOWER_IDX, lower.indexOfSelf());
        });

        var upperId = attributes().getOptionalString(substring, "upper", FlangName.SUBSTRING_RANGE);
        var upperExpr = upperId.map(this::getChild);
        upperExpr.ifPresent(upper -> {
            substring.addChild(upper);
            substring.setOptional(Substring.UPPER_IDX, upper.indexOfSelf());
        });
    }
}
