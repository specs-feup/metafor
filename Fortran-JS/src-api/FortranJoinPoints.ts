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
}