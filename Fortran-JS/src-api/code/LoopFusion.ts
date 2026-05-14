import { DoStatement } from "../Joinpoints.js";

/**
 * Fuses an array of do-loops with identical range controls into a single loop.
 *
 * All statements from loops[1..n] are appended to loops[0]'s body, and the
 * remaining loops are detached from the AST.
 *
 * @throws If fewer than 2 loops are supplied, any loop is not a range loop, or
 *   the loop boundaries differ.
 *
 * @example
 * // Before:
 * // do i = 1, n
 * //   a(i) = b(i)
 * // end do
 * // do i = 1, n
 * //   c(i) = d(i)
 * // end do
 *
 * // After:
 * // do i = 1, n
 * //   a(i) = b(i)
 * //   c(i) = d(i)
 * // end do
 *
 * @param $loops - Two or more do-loops to fuse; all must share the same range control.
 * @returns The surviving first loop with all statements merged in.
 */
export default function loopFusion($loops: DoStatement[]): DoStatement {
  if ($loops.length < 2) throw new Error("Need at least 2 loops to fuse");

  if ($loops.some((loop) => loop.kind !== 'range')) {
    throw new Error("All loops must have RangeLoopControl");
  }
  const firstLoop = $loops[0];

  for (const loop of $loops.slice(1)) {
    if (!firstLoop.sameScope(loop))
      throw new Error("All loops must have the same boundaries");
  }

  for (const loop of $loops.slice(1)) {
    for (const stmt of loop.body.executableStmts) {
      firstLoop.body.insertEnd(stmt);
    }
    loop.detach();
  }

  return firstLoop;
}
