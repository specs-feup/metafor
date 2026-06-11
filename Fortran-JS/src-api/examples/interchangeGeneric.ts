import Query from "@specs-feup/lara/api/weaver/Query.js";
import { DoStatement, RangeLoopControl, Joinpoint } from "../Joinpoints.js";

function isInKernelSubroutine(loop: DoStatement): boolean {
  const sub = loop.getAncestor("programUnit");
  if (!sub) return false;
  return (sub as Joinpoint).code.split("\n")[0].toLowerCase().includes("kernel_");
}

const pairs: { outer: DoStatement; inner: DoStatement }[] = [];
for (const loop of [...Query.search(DoStatement)].filter(isInKernelSubroutine)) {
  const stmts = loop.body.executableStmts;
  if (stmts.length === 1 && stmts[0] instanceof DoStatement) {
    pairs.push({ outer: loop, inner: stmts[0] as DoStatement });
  }
}

const innerSet = new Set(pairs.map(p => p.inner));
const topPairs = pairs.filter(p => !innerSet.has(p.outer));

console.log(`[interchangeGeneric] Found ${topPairs.length} interchangeable loop pair(s)`);

for (const { outer, inner } of topPairs) {
  const oc = outer.control;
  const ic = inner.control;
  if (!(oc instanceof RangeLoopControl && ic instanceof RangeLoopControl)) continue;

  const innerBody = inner.body.executableStmts.map(s => (s as Joinpoint).code);

  const newCode = [
    `do ${ic.code}`,
    `do ${oc.code}`,
    ...innerBody,
    `end do`,
    `end do`,
  ].join("\n");

  outer.insert("replace", newCode);
  console.log(`  [interchangeGeneric] Interchanged: ${oc.var.name} <-> ${ic.var.name}`);
}
