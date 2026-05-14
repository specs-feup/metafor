import Query from "@specs-feup/lara/api/weaver/Query.js";
import { DoStatement, RangeLoopControl, Joinpoint } from "./Joinpoints.js";

function isInKernel3mm(loop: DoStatement): boolean {
  const sub = loop.getAncestor("programUnit");
  if (!sub) return false;
  return sub.code.split("\n")[0].toLowerCase().includes("kernel_3mm");
}

const loops = [...Query.search(DoStatement)].filter(isInKernel3mm);
const toSkip = new Set<DoStatement>();
let count = 0;

for (const loop of loops) {
  if (toSkip.has(loop)) continue;

  const ctrl = loop.control;
  if (!(ctrl instanceof RangeLoopControl)) continue;

  const nextJp = loop.rightJp;
  if (!(nextJp instanceof DoStatement)) continue;

  const next = nextJp as DoStatement;
  if (toSkip.has(next)) continue;

  const nextCtrl = next.control;
  if (!(nextCtrl instanceof RangeLoopControl)) continue;

  // Only fuse when the loop control expressions are identical
  if (ctrl.code !== nextCtrl.code) continue;

  const body1 = loop.body.executableStmts.map(s => (s as Joinpoint).code);
  const body2 = next.body.executableStmts.map(s => (s as Joinpoint).code);

  const merged = [
    `do ${ctrl.code}`,
    ...body1,
    ...body2,
    `end do`,
  ].join("\n");

  loop.insert("replace", merged);
  next.insert("replace", "");
  toSkip.add(next);
  count++;
  console.log(`  Fused consecutive loops: do ${ctrl.code}`);
}

console.log(`Fused ${count} loop pair(s)`);
console.log("\n=== MODIFIED CODE ===");
console.log((Query.root() as Joinpoint).code);
