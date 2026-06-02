import { DoStatement, RangeLoopControl } from "../Joinpoints.js";

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

export function canFission($loop: DoStatement): boolean {
  return $loop.control instanceof RangeLoopControl
    && $loop.body.executableStmts.length > 1;
}
