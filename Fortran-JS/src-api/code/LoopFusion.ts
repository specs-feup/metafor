import { DoStatement, RangeLoopControl } from "../Joinpoints.js";

export default function loopFusion($loop1: DoStatement, $loop2: DoStatement): DoStatement {
  if (!($loop1.control instanceof RangeLoopControl) || !($loop2.control instanceof RangeLoopControl)) {
    throw new Error("Both loops should of the same type")
  }

  const ctrl1 = $loop1.control as RangeLoopControl
  const ctrl2 = $loop2.control as RangeLoopControl

  if (ctrl1.code !== ctrl2.code) {
    throw new Error("Both loops should have the same boundaries")
  }

  for (const statement of $loop2.body.executableStmts) {
    $loop1.body.insertEnd(statement)
  }

  $loop2.detach()

  return $loop1
}
