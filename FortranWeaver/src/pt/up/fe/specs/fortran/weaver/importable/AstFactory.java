package pt.up.fe.specs.fortran.weaver.importable;

import pt.up.fe.specs.fortran.ast.nodes.expr.Argument;
import pt.up.fe.specs.fortran.ast.nodes.expr.Call;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpClauseKind;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.stmt.DoStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.FortranWeaver;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.*;
import pt.up.fe.specs.util.SpecsCollections;

import java.util.List;
import java.util.stream.Collectors;

public class AstFactory {
    public static AOmpLoopConstruct ompLoopConstruct(ADoStatement loop, Object[] args) {
        List<OmpClause> clauses = SpecsCollections.asListT(AOmpClause.class, args)
                .stream()
                .map(clause -> (OmpClause) clause.getNode())
                .toList();

        DoStmt doStmt = (DoStmt) loop.getNode();

        return FortranJoinpoints.create(FortranWeaver.getFactory().ompLoopConstruct(doStmt, clauses), AOmpLoopConstruct.class);
    }

    public static AOmpLoopConstruct emptyOmpLoopConstruct() {
        return FortranJoinpoints.create(FortranWeaver.getFactory().emptyOmpLoopConstruct(), AOmpLoopConstruct.class);
    }

    public static AOmpBlockConstruct emptyOmpBlockConstruct() {
        return FortranJoinpoints.create(FortranWeaver.getFactory().emptyOmpBlockConstruct(), AOmpBlockConstruct.class);
    }

    public static AOmpDataSharingClause ompPrivateClause(Object[] args) {
        List<DataRef> dataRefs = SpecsCollections.asListT(ADataRef.class, args)
                .stream()
                .map(clause -> (DataRef) clause.getNode())
                .toList();

        return FortranJoinpoints.create(FortranWeaver.getFactory().ompDataSharingClause(OmpClauseKind.PRIVATE, dataRefs), AOmpDataSharingClause.class);
    }

    public static ADataRef dataRef(String name) {
        return FortranJoinpoints.create(FortranWeaver.getFactory().dataRef(name), ADataRef.class);
    }

    public static AUseStatement useStatement(String moduleName) {
        return FortranJoinpoints.create(FortranWeaver.getFactory().useStmt(moduleName), AUseStatement.class);
    }

    public static AOmpReductionClause ompReductionClause(String operator, Object[] args) {
        List<DataRef> dataRefs = SpecsCollections.asListT(ADataRef.class, args)
                .stream()
                .map(clause -> (DataRef) clause.getNode())
                .toList();

        BinaryOperatorKind kind = BinaryOperatorKind.valueOf(operator);

        return FortranJoinpoints.create(FortranWeaver.getFactory().ompReductionClause(kind, dataRefs), AOmpReductionClause.class);
    }

    public static AExecution execution(Object[] args) {
        List<ExecutableStmt> stmts = SpecsCollections.asListT(AExecutableStatement.class, args)
                .stream()
                .map(stmt -> (ExecutableStmt) stmt.getNode())
                .toList();

        return FortranJoinpoints.create(FortranWeaver.getFactory().execution(stmts), AExecution.class);
    }

    public static AOmpOrderedClause ompOrderedClause(int value) {
        return FortranJoinpoints.create(FortranWeaver.getFactory().ompOrderedClause(value), AOmpOrderedClause.class);
    }

    public static AIntLiteral intLiteral(int value) {
        return FortranJoinpoints.create(FortranWeaver.getFactory().intLiteral(value), AIntLiteral.class);
    }

    public static ABinaryOperator binaryOperatorAdd(AExpr lhs, AExpr rhs) {
        return binaryOperator(BinaryOperatorKind.ADD, lhs, rhs);
    }

    public static ABinaryOperator binaryOperatorSubtract(AExpr lhs, AExpr rhs) {
        return binaryOperator(BinaryOperatorKind.SUBTRACT, lhs, rhs);
    }

    public static ABinaryOperator binaryOperatorMultiply(AExpr lhs, AExpr rhs) {
        return binaryOperator(BinaryOperatorKind.MULTIPLY, lhs, rhs);
    }

    public static ABinaryOperator binaryOperatorDivide(AExpr lhs, AExpr rhs) {
        return binaryOperator(BinaryOperatorKind.DIVIDE, lhs, rhs);
    }

    public static ARangeLoopControl rangeLoopControl(ADataRef var, AExpr lower, AExpr upper) {
        DataRef varNode = (DataRef) var.getNode();
        Expr lowerNode = (Expr) lower.getNode();
        Expr upperNode = (Expr) upper.getNode();
        return FortranJoinpoints.create(
            FortranWeaver.getFactory().rangeLoopControl(varNode, lowerNode, upperNode),
            ARangeLoopControl.class
        );
    }

    public static ADoStatement doStatement(ARangeLoopControl control) {
        RangeLoopControl ctrl = (RangeLoopControl) control.getNode();
        return FortranJoinpoints.create(
            FortranWeaver.getFactory().doStatement(ctrl),
            ADoStatement.class
        );
    }

    public static AExpr intrinsicCall(String name, Object[] args) {
        DataRef callee = FortranWeaver.getFactory().dataRef(name);
        List<Argument> argNodes = SpecsCollections.asListT(AExpr.class, args)
                .stream()
                .map(a -> FortranWeaver.getFactory().argument((Expr) a.getNode()))
                .collect(Collectors.toList());
        return FortranJoinpoints.create(
                FortranWeaver.getFactory().functionCall(callee, argNodes),
                AExpr.class);
    }

    private static ABinaryOperator binaryOperator(BinaryOperatorKind kind, AExpr lhs, AExpr rhs) {
        Expr lhsNode = (Expr) lhs.getNode();
        Expr rhsNode = (Expr) rhs.getNode();
        return FortranJoinpoints.create(
            FortranWeaver.getFactory().binaryOperator(kind, lhsNode, rhsNode),
            ABinaryOperator.class
        );
    }
}
