import { DoStatement, RangeLoopControl } from "../Joinpoints.js";

export default function loopFision($loop: DoStatement): DoStatement[] {
  const copiedLoopScopeTemplate = $loop.copyScope()
  const statements = $loop.body.executableStmts
  const result: DoStatement[] = []

  if (!($loop.control instanceof RangeLoopControl)) {
    return [$loop]
  }
  
  if (statements.length <= 1) {
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