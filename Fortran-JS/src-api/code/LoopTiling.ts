import Query from "@specs-feup/lara/api/weaver/Query.js";
import FortranJoinPoints from "../FortranJoinPoints.js";
import {
  ArraySubscriptExpr,
  DataRef,
  DoStatement,
  EntityDecl,
  ExecutableStatement,
  RangeLoopControl,
} from "../Joinpoints.js";
import {
  controlExpressions,
  hasLoopCarriedDependency,
  referencesName,
  summarizeLoop,
} from "./LoopAnalysis.js";

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
  if (!Number.isInteger(tileSize) || tileSize <= 0 || !canTile(outer, inner)) {
    return [outer];
  }
  const oc = outer.control as RangeLoopControl;
  const ic = inner.control as RangeLoopControl;

  const usedNames = new Set([
    ...[...Query.searchFromInclusive(outer.root, DataRef)].map((reference) =>
      reference instanceof ArraySubscriptExpr ? reference.var.name : reference.name,
    ),
    ...[...Query.searchFromInclusive(outer.root, EntityDecl)].map((declaration) => declaration.name),
  ]);
  const ovt = freshName(`${oc.var.name}_tile`, usedNames);
  usedNames.add(ovt);
  const ivt = freshName(`${ic.var.name}_tile`, usedNames);

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

function freshName(preferred: string, usedNames: Set<string>): string {
  let name = preferred;
  let suffix = 2;
  while (usedNames.has(name)) {
    name = `${preferred}_${suffix}`;
    suffix++;
  }
  return name;
}

export function canTile(outer: DoStatement, inner: DoStatement): boolean {
  const oc = outer.control, ic = inner.control;
  if (!(oc instanceof RangeLoopControl && ic instanceof RangeLoopControl)) return false;
  const outerBody = outer.body.executableStmts;
  if (outerBody.length !== 1 || !(outerBody[0] instanceof DoStatement)) return false;
  if (!outerBody[0].equals(inner)) return false;
  if (oc.var.name === ic.var.name) return false;
  if (oc.step !== undefined || ic.step !== undefined) return false;

  const outerVar = oc.var.name;

  // A control expression that references the outer variable describes a
  // triangular or otherwise non-rectangular iteration space. Strip-mining it
  // would change the set of iterations.
  if (controlExpressions(ic).some((expression) => referencesName(expression, outerVar))) {
    return false;
  }

  // Nested controls are also evaluated at a different point after tiling.
  for (const nested of Query.searchFrom(inner.body, DoStatement)) {
    const nc = nested.control;
    if (!(nc instanceof RangeLoopControl)) continue;
    if (controlExpressions(nc).some((expression) => referencesName(expression, outerVar))) {
      return false;
    }
  }

  return !hasLoopCarriedDependency(summarizeLoop(inner));
}
