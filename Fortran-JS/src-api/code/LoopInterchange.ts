import Query from "@specs-feup/lara/api/weaver/Query.js";
import FortranJoinPoints from "../FortranJoinPoints.js";
import { DataRef, DoStatement, ExecutableStatement, RangeLoopControl } from "../Joinpoints.js";
import {
  controlExpressions,
  hasLoopCarriedDependency,
  referencesName,
  summarizeLoop,
} from "./LoopAnalysis.js";

/**
 * Swaps the outer and inner loop of a perfect 2-deep nest.
 *
 * Transforms:
 * ```fortran
 * do i = lo_i, hi_i
 *   do j = lo_j, hi_j
 *     body
 *   end do
 * end do
 * ```
 * Into:
 * ```fortran
 * do j = lo_j, hi_j
 *   do i = lo_i, hi_i
 *     body
 *   end do
 * end do
 * ```
 *
 * Returns unchanged [outer] if canInterchange() fails.
 */
export default function loopInterchange(outer: DoStatement, inner: DoStatement): DoStatement[] {
  if (!canInterchange(outer, inner)) return [outer];
  const oc = outer.control as RangeLoopControl;
  const ic = inner.control as RangeLoopControl;

  // New outer uses inner's control (deep-copied)
  const newOuterCtrl = FortranJoinPoints.rangeLoopControl(
    ic.var.deepCopy() as DataRef,
    ic.lower.deepCopy(),
    ic.upper.deepCopy()
  );
  if (ic.step !== undefined) newOuterCtrl.setStep(ic.step.deepCopy());
  const newOuterDo = FortranJoinPoints.doStatement(newOuterCtrl);

  // New inner uses outer's control (deep-copied)
  const newInnerCtrl = FortranJoinPoints.rangeLoopControl(
    oc.var.deepCopy() as DataRef,
    oc.lower.deepCopy(),
    oc.upper.deepCopy()
  );
  if (oc.step !== undefined) newInnerCtrl.setStep(oc.step.deepCopy());
  const newInnerDo = FortranJoinPoints.doStatement(newInnerCtrl);

  // Deep-copy body statements into new inner loop
  for (const stmt of inner.body.executableStmts) {
    newInnerDo.body.insertEnd(stmt.deepCopy() as ExecutableStatement);
  }
  newOuterDo.body.insertEnd(newInnerDo);
  outer.replaceWith(newOuterDo);
  return [newOuterDo];
}

export function canInterchange(outer: DoStatement, inner: DoStatement): boolean {
  const oc = outer.control, ic = inner.control;
  if (!(oc instanceof RangeLoopControl && ic instanceof RangeLoopControl)) return false;
  const outerBody = outer.body.executableStmts;
  if (outerBody.length !== 1 || !(outerBody[0] instanceof DoStatement)) return false;
  if (!outerBody[0].equals(inner)) return false;

  const outerVar = oc.var.name;
  const innerVar = ic.var.name;
  if (outerVar === innerVar) return false;

  // A control expression that references the other loop variable changes its
  // meaning when that control is moved to the other loop level.
  if (controlExpressions(ic).some((expression) => referencesName(expression, outerVar))) {
    return false;
  }
  if (controlExpressions(oc).some((expression) => referencesName(expression, innerVar))) {
    return false;
  }

  // Nested loop controls are evaluated at a different point after interchange.
  for (const nested of Query.searchFrom(inner.body, DoStatement)) {
    const nc = nested.control;
    if (!(nc instanceof RangeLoopControl)) continue;
    if (controlExpressions(nc).some((expression) => referencesName(expression, outerVar))) {
      return false;
    }
  }

  // Reordering a loop nest with a dependence in its body changes the order in
  // which values become available. Without an affine dependence solver, reject
  // bodies with a detectable loop-carried read/write dependence.
  if (hasLoopCarriedDependency(summarizeLoop(inner))) return false;

  return true;
}
