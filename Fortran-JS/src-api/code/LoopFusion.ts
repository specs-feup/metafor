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
  if (!canFuse($loops)) throw new Error("Loops cannot be fused: need ≥2 range loops with identical boundaries");

  const firstLoop = $loops[0];

  for (const loop of $loops.slice(1)) {
    for (const stmt of loop.body.executableStmts) {
      firstLoop.body.insertEnd(stmt);
    }
    loop.detach();
  }

  return firstLoop;
}

export function canFuse($loops: DoStatement[]): boolean {
  if ($loops.length < 2) return false;
  if ($loops.some(l => l.kind !== 'range')) return false;
  return $loops.slice(1).every(l => $loops[0].sameScope(l));
}
