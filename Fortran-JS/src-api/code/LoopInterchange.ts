import Query from "@specs-feup/lara/api/weaver/Query.js";
import FortranJoinPoints from "../FortranJoinPoints.js";
import { DataRef, DoStatement, ExecutableStatement, RangeLoopControl } from "../Joinpoints.js";

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
  const outerVar = oc.var.name;

  // Check 1: triangular inner bounds — inner bound references outer variable
  if (ic.lower.code.includes(outerVar) || ic.upper.code.includes(outerVar)) return false;

  // Check 2: nested DO inside body uses outer variable in its bounds
  for (const nested of Query.searchFrom(inner.body, DoStatement)) {
    const nc = nested.control;
    if (!(nc instanceof RangeLoopControl)) continue;
    if (nc.lower.code.includes(outerVar) || nc.upper.code.includes(outerVar)) return false;
  }
  return true;
}
