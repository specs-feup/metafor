package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
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
    }

    public void logicalLiteral(LogicalLiteral logicalLiteral) {
        logicalLiteral.set(StringLiteral.SOURCE_LITERAL, attributes().getString(logicalLiteral, "bool"));
    }

    public void parenExpr(ParenExpr parenExpr) {
        parenExpr.addChild(getChild(parenExpr, FlangName.EXPR));
    }

    public void binaryOperator(BinaryOperator binaryOperator) {
        binaryOperator.addChild(getChild(binaryOperator, "left"));
        binaryOperator.addChild(getChild(binaryOperator, "right"));

        String opName = attributes().getString(binaryOperator, "op");

        binaryOperator.set(BinaryOperator.OP, BinaryOperatorKind.valueOf(opName));
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

    public void arraySubscriptExpr(ArraySubscriptExpr arraySubscriptExpr) {
        arraySubscriptExpr.addChild(getChild(arraySubscriptExpr, "base"));
        arraySubscriptExpr.addChildren(getChildren(arraySubscriptExpr, "subscripts"));
    }
}
