import Query from "@specs-feup/lara/api/weaver/Query.js";
import FortranJoinPoints from "../FortranJoinPoints.js";
import { DataRef, DoStatement, ExecutableStatement, RangeLoopControl } from "../Joinpoints.js";

/**
 * Deep-copies a body statement and replaces every reference to `varName` with
 * `varName+offset` using AST-level substitution (only real variable references
 * are affected, not occurrences inside string literals or comments).
 */
function substituteVar(stmt: ExecutableStatement, varName: string, offset: number): ExecutableStatement {
  const copy = stmt.deepCopy() as ExecutableStatement;
  if (offset === 0) return copy;
  for (const ref of Query.searchFrom(copy, DataRef, { name: varName })) {
    const binOp = FortranJoinPoints.binaryOperatorAdd(ref.deepCopy(), FortranJoinPoints.intLiteral(offset));
    ref.replaceWith(binOp)
  }
  return copy;
}

/**
 * Unrolls a range do-loop by the given factor.
 *
 * Two loops replace the original:
 *  - A **main loop** that steps by `factor`, with the body replicated `factor`
 *    times (each copy substitutes `var` with `var+offset` for offset 0..factor-1).
 *  - A **cleanup loop** that handles the remaining < `factor` iterations with the
 *    original body. When `factor` exactly divides the trip count, the cleanup
 *    loop's bounds produce a no-op and it generates no iterations.
 *
 * If the loop is not a range loop, has an explicit non-unit step, or `factor`
 * is ≤ 1, the loop is left unchanged and returned as a single-element array.
 *
 * @example
 * // Before (factor = 4):
 * // do i = 1, n
 * //   a(i) = b(i)
 * // end do
 *
 * // After:
 * // do i = 1, (n) - 3, 4
 * //   a(i) = b(i); a(i+1) = b(i+1); a(i+2) = b(i+2); a(i+3) = b(i+3)
 * // end do
 * // do i = (1) + (((n) - (1) + 1) / 4) * 4, n
 * //   a(i) = b(i)
 * // end do
 *
 * @param $loop - The do-loop to unroll.
 * @param factor - The unroll factor (must be ≥ 2).
 * @returns The replacement loops inserted into the AST, in source order.
 */
export default function loopUnroll($loop: DoStatement, factor: number): DoStatement[] {
  if (!canUnroll($loop, factor)) return [$loop];
  const ctrl = $loop.control as RangeLoopControl;
  const v = ctrl.var.name;
  const bodyStmts = $loop.body.executableStmts;

  // Main loop: steps by factor; upper bound guarantees all factor copies stay within hi.
  const mainDo = $loop.copyScope()
  const mainCtrl = mainDo.control as RangeLoopControl;

  mainCtrl.setUpper(FortranJoinPoints.binaryOperatorSubtract(
    ctrl.upper.deepCopy(), 
    FortranJoinPoints.intLiteral(factor - 1))
  );
  mainCtrl.setStep(FortranJoinPoints.intLiteral(factor));

  for (let off = 0; off < factor; off++) {
    for (const stmt of bodyStmts) {
      mainDo.body.insertEnd(substituteVar(stmt, v, off));
    }
  }

  // Cleanup loop: lo + ((hi - lo + 1) / factor) * factor  ..  hi
  // Integer division makes this a no-op when factor exactly divides the trip count.
  const cleanupLower = FortranJoinPoints.binaryOperatorAdd(
    ctrl.lower.deepCopy(),
    FortranJoinPoints.binaryOperatorMultiply(
      FortranJoinPoints.binaryOperatorDivide(
        FortranJoinPoints.binaryOperatorAdd(
          FortranJoinPoints.binaryOperatorSubtract(ctrl.upper.deepCopy(), ctrl.lower.deepCopy()),
          FortranJoinPoints.intLiteral(1)
        ),
        FortranJoinPoints.intLiteral(factor)
      ),
      FortranJoinPoints.intLiteral(factor)
    )
  );
  const cleanupCtrl = FortranJoinPoints.rangeLoopControl(ctrl.var.deepCopy() as DataRef, cleanupLower, ctrl.upper.deepCopy());
  const cleanupDo = FortranJoinPoints.doStatement(cleanupCtrl);
  for (const stmt of bodyStmts) {
    cleanupDo.body.insertEnd(substituteVar(stmt, v, 0));
  }

  $loop.replaceWith([mainDo, cleanupDo])
  return [mainDo, cleanupDo];
}

export function canUnroll($loop: DoStatement, factor: number = 2): boolean {
  if (!($loop.control instanceof RangeLoopControl)) return false;
  if (factor <= 1) return false;
  return ($loop.control as RangeLoopControl).step === undefined;
}