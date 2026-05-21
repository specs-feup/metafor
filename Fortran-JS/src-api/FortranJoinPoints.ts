import * as Joinpoints from "./Joinpoints.js";
import {unwrapJoinPoint, wrapJoinPoint} from "@specs-feup/lara/api/LaraJoinPoint.js";
import FortranJavaTypes from "./FortranJavaTypes.js";
import {flattenArgsArray} from "@specs-feup/lara/api/lara/core/LaraCore.js";

export default class FortranJoinPoints {
    static ompLoopConstruct(loop: Joinpoints.DoStatement, clauses: Joinpoints.OmpClause[]): Joinpoints.OmpLoopConstruct {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.ompLoopConstruct(
            unwrapJoinPoint(loop),
            flattenArgsArray(clauses).map(unwrapJoinPoint)
            ));
    }

    static emptyOmpLoopConstruct(): Joinpoints.OmpLoopConstruct {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.emptyOmpLoopConstruct());
    }

    static ompPrivateClause(dataRefs: Joinpoints.DataRef[]): Joinpoints.OmpDataSharingClause {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.ompPrivateClause(
            flattenArgsArray(dataRefs).map(unwrapJoinPoint)
        ));
    }

    static dataRef(name: string): Joinpoints.DataRef {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.dataRef(name));
    }

    static useStatement(moduleName: string): Joinpoints.UseStatement {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.useStatement(moduleName));
    }

    static ompReductionClause(operator: string, dataRefs: Joinpoints.DataRef[]): Joinpoints.OmpReductionClause {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.ompReductionClause(
            operator,
            flattenArgsArray(dataRefs).map(unwrapJoinPoint)
        ));
    }

    static emptyOmpBlockConstruct(): Joinpoints.OmpBlockConstruct {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.emptyOmpBlockConstruct());
    }

    static execution(stmts: Joinpoints.ExecutableStatement[]): Joinpoints.Execution {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.execution(
            flattenArgsArray(stmts).map(unwrapJoinPoint)
        ));
    }

    static ompOrderedClause(value: number): Joinpoints.OmpOrderedClause {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.ompOrderedClause(value));
    }

    static intLiteral(value: number): Joinpoints.IntLiteral {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.intLiteral(value));
    }

    static binaryOperatorAdd(lhs: Joinpoints.Expr, rhs: Joinpoints.Expr): Joinpoints.BinaryOperator {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.binaryOperatorAdd(
            unwrapJoinPoint(lhs),
            unwrapJoinPoint(rhs)
        ));
    }

    static binaryOperatorSubtract(lhs: Joinpoints.Expr, rhs: Joinpoints.Expr): Joinpoints.BinaryOperator {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.binaryOperatorSubtract(
            unwrapJoinPoint(lhs),
            unwrapJoinPoint(rhs)
        ));
    }

    static binaryOperatorMultiply(lhs: Joinpoints.Expr, rhs: Joinpoints.Expr): Joinpoints.BinaryOperator {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.binaryOperatorMultiply(
            unwrapJoinPoint(lhs),
            unwrapJoinPoint(rhs)
        ));
    }

    static binaryOperatorDivide(lhs: Joinpoints.Expr, rhs: Joinpoints.Expr): Joinpoints.BinaryOperator {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.binaryOperatorDivide(
            unwrapJoinPoint(lhs),
            unwrapJoinPoint(rhs)
        ));
    }

    static rangeLoopControl(var_: Joinpoints.DataRef, lower: Joinpoints.Expr, upper: Joinpoints.Expr): Joinpoints.RangeLoopControl {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.rangeLoopControl(
            unwrapJoinPoint(var_), unwrapJoinPoint(lower), unwrapJoinPoint(upper)
        ));
    }

    static doStatement(control: Joinpoints.RangeLoopControl): Joinpoints.DoStatement {
        return wrapJoinPoint(FortranJavaTypes.AstFactory.doStatement(unwrapJoinPoint(control)));
    }
}