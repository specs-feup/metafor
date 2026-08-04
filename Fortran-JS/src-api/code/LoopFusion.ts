import { DoStatement } from "../Joinpoints.js";
import {
  hasCrossGroupDependency,
  summarizeLoop,
} from "./LoopAnalysis.js";

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
  if (!canFuse($loops)) {
    throw new Error("Loops cannot be fused: incompatible ranges, scopes, or dependencies");
  }

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
  if ($loops.some((loop) => loop.kind !== "range")) return false;
  if (!$loops.slice(1).every((loop) => $loops[0].sameScope(loop))) {
    return false;
  }

  for (let first = 0; first < $loops.length; first++) {
    for (let second = first + 1; second < $loops.length; second++) {
      if (!canFusePair($loops[first], $loops[second])) return false;
    }
  }
  return true;
}

export function canFusePair(first: DoStatement, second: DoStatement): boolean {
  if (first.kind !== "range" || second.kind !== "range") return false;
  if (!first.sameScope(second)) return false;
  if (!first.parent?.equals(second.parent)) return false;

  return !hasCrossGroupDependency(summarizeLoop(first), summarizeLoop(second));
}
