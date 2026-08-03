import Query from "@specs-feup/lara/api/weaver/Query.js";
import { ArraySubscriptExpr, AssignmentStatement, DoStatement, Joinpoint,
         RangeLoopControl } from "../Joinpoints.js";

/**
 * Splits a do-loop with multiple body statements into one loop per statement.
 *
 * If the loop has a single body statement or is not a range loop, it is
 * returned unchanged.
 *
 * @example
 * // Before:
 * // do i = 1, n
 * //   a(i) = b(i)
 * //   c(i) = d(i)
 * // end do

 * // After:
 * // do i = 1, n
 * //   a(i) = b(i)
 * // end do
 * // do i = 1, n
 * //   c(i) = d(i)
 * // end do
 *
 * @param $loop - The do-loop to split.
 * @returns The replacement loops inserted into the AST, in source order.
 */
export default function loopFission($loop: DoStatement): DoStatement[] {
  const copiedLoopScopeTemplate = $loop.copyScope()
  const statements = $loop.body.executableStmts
  const result: DoStatement[] = []

  if (!canFission($loop)) {
    return [$loop]
  }

  for (const statement of statements) {
    const newCopy = copiedLoopScopeTemplate.copyScope()
    newCopy.body.insertBegin(statement)
    result.push(newCopy)
  }

  $loop.replaceWith(result)

  return result
}

function stmtArrayWrites(stmt: Joinpoint): Set<string> {
  const result = new Set<string>();
  for (const assign of Query.searchFromInclusive(stmt, AssignmentStatement)) {
    const lhs = assign.variable;
    if (lhs instanceof ArraySubscriptExpr) result.add(lhs.var.name);
  }
  return result;
}

function stmtArrayReads(stmt: Joinpoint): Set<string> {
  const result = new Set<string>();
  for (const assign of Query.searchFromInclusive(stmt, AssignmentStatement)) {
    for (const expr of Query.searchFromInclusive(assign.expr, ArraySubscriptExpr)) {
      result.add(expr.var.name);
    }
  }
  return result;
}

function stmtScalarWrites(stmt: Joinpoint): string[] {
  const result: string[] = [];
  for (const assign of Query.searchFromInclusive(stmt, AssignmentStatement)) {
    const lhs = assign.variable;
    if (!(lhs instanceof ArraySubscriptExpr)) result.push(lhs.name);
  }
  return result;
}

export function canFission($loop: DoStatement): boolean {
  if (!($loop.control instanceof RangeLoopControl)) return false;
  const stmts = [...$loop.body.executableStmts];
  if (stmts.length <= 1) return false;

  const arrayReads   = stmts.map(s => stmtArrayReads(s));
  const arrayWrites  = stmts.map(s => stmtArrayWrites(s));
  const scalarWrites = stmts.map(s => stmtScalarWrites(s));

  for (let j = 1; j < stmts.length; j++) {
    // Check 1: scalar written in an earlier stmt appears in a later stmt's code.
    // After fission, the later loop sees only the scalar value from its own
    // iteration — it cannot observe the value threaded from the earlier loop.
    for (let i = 0; i < j; i++) {
      for (const sv of scalarWrites[i]) {
        if (new RegExp(`\\b${sv}\\b`).test(stmts[j].code)) return false;
      }
    }

    // Check 2: a later stmt writes an array that an earlier stmt reads.
    // After fission, the earlier loop (for ALL iterations) runs before the
    // later loop, so the earlier loop reads stale values for every iteration
    // after the first one that the later loop would have updated.
    for (const wName of arrayWrites[j]) {
      for (let i = 0; i < j; i++) {
        if (arrayReads[i].has(wName)) return false;
      }
    }
  }

  return true;
}
