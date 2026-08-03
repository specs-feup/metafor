import FortranJoinPoints from "../FortranJoinPoints.js";
import { DataRef, DoStatement, ExecutableStatement, RangeLoopControl } from "../Joinpoints.js";

/**
 * Tiles a perfect 2-deep loop nest by `tileSize`.
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
 * do ii = lo_i, hi_i, TILE
 *   do jj = lo_j, hi_j, TILE
 *     do i = ii, MIN(ii + TILE - 1, hi_i)
 *       do j = jj, MIN(jj + TILE - 1, hi_j)
 *         body
 *       end do
 *     end do
 *   end do
 * end do
 * ```
 *
 * Returns unchanged `[outer]` if either loop is not a unit-step range loop.
 */
export default function loopTile(outer: DoStatement, inner: DoStatement, tileSize: number): DoStatement[] {
  if (!canTile(outer, inner)) return [outer];
  const oc = outer.control as RangeLoopControl;
  const ic = inner.control as RangeLoopControl;

  const ovt = oc.var.name.repeat(2);
  const ivt = ic.var.name.repeat(2);

  // Outer tile loop: ii = lo_i, hi_i, TILE
  const outerTileCtrl = FortranJoinPoints.rangeLoopControl(
    FortranJoinPoints.dataRef(ovt), oc.lower.deepCopy(), oc.upper.deepCopy());
  outerTileCtrl.setStep(FortranJoinPoints.intLiteral(tileSize));
  const outerTileDo = FortranJoinPoints.doStatement(outerTileCtrl);

  // Inner tile loop: jj = lo_j, hi_j, TILE
  const innerTileCtrl = FortranJoinPoints.rangeLoopControl(
    FortranJoinPoints.dataRef(ivt), ic.lower.deepCopy(), ic.upper.deepCopy());
  innerTileCtrl.setStep(FortranJoinPoints.intLiteral(tileSize));
  const innerTileDo = FortranJoinPoints.doStatement(innerTileCtrl);

  // Original outer as inner: i = ii, MIN(ii + TILE - 1, hi_i)
  const outerInnerUpper = FortranJoinPoints.intrinsicCall("MIN", [
    FortranJoinPoints.binaryOperatorSubtract(
      FortranJoinPoints.binaryOperatorAdd(
        FortranJoinPoints.dataRef(ovt),
        FortranJoinPoints.intLiteral(tileSize)),
      FortranJoinPoints.intLiteral(1)),
    oc.upper.deepCopy()]);
  const outerInnerCtrl = FortranJoinPoints.rangeLoopControl(
    oc.var.deepCopy() as DataRef,
    FortranJoinPoints.dataRef(ovt),
    outerInnerUpper);
  const outerInnerDo = FortranJoinPoints.doStatement(outerInnerCtrl);

  // Original inner as innermost: j = jj, MIN(jj + TILE - 1, hi_j)
  const innerInnerUpper = FortranJoinPoints.intrinsicCall("MIN", [
    FortranJoinPoints.binaryOperatorSubtract(
      FortranJoinPoints.binaryOperatorAdd(
        FortranJoinPoints.dataRef(ivt),
        FortranJoinPoints.intLiteral(tileSize)),
      FortranJoinPoints.intLiteral(1)),
    ic.upper.deepCopy()]);
  const innerInnerCtrl = FortranJoinPoints.rangeLoopControl(
    ic.var.deepCopy() as DataRef,
    FortranJoinPoints.dataRef(ivt),
    innerInnerUpper);
  const innerInnerDo = FortranJoinPoints.doStatement(innerInnerCtrl);

  for (const stmt of inner.body.executableStmts) {
    innerInnerDo.body.insertEnd(stmt.deepCopy() as ExecutableStatement);
  }
  outerInnerDo.body.insertEnd(innerInnerDo);
  innerTileDo.body.insertEnd(outerInnerDo);
  outerTileDo.body.insertEnd(innerTileDo);
  outer.replaceWith(outerTileDo);
  return [outerTileDo];
}

export function canTile(outer: DoStatement, inner: DoStatement): boolean {
  const oc = outer.control, ic = inner.control;
  return oc instanceof RangeLoopControl && ic instanceof RangeLoopControl
    && oc.step === undefined && ic.step === undefined;
}