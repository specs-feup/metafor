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
        arraySubscriptExpr.addChild(getChild(arraySubscriptExpr, "base"));
        arraySubscriptExpr.addChildren(getChildren(arraySubscriptExpr, "subscripts"));
    }

    public void subscript(Subscript subscript) {
        var value = getChild(subscript, FlangName.EXPR);
        subscript.addChild(value);
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
}
