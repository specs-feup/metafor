package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.UnaryOperatorKind;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;
import pt.up.fe.specs.util.collections.Attributes;

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

    public void binaryOperator(BinaryOperator binaryOperator) {
        binaryOperator.addChild(getChild(binaryOperator, "left"));
        binaryOperator.addChild(getChild(binaryOperator, "right"));

        String opName = attributes().getString(binaryOperator, "op");

        binaryOperator.set(BinaryOperator.OP, BinaryOperatorKind.valueOf(opName));
    }

    public void unaryOperator(UnaryOperator unaryOperator) {
        unaryOperator.addChild(getChild(unaryOperator, FlangName.EXPR));

        String opName = attributes().getString(unaryOperator, "op");

        unaryOperator.set(UnaryOperator.OP, UnaryOperatorKind.valueOf(opName));
    }

}
