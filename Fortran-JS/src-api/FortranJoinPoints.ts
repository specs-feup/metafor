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
}