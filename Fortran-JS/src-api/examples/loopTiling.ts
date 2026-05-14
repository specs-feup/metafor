import Query from "@specs-feup/lara/api/weaver/Query.js";
import { DoStatement, RangeLoopControl, Joinpoint } from "./Joinpoints.js";

const T = 32;

function isInKernel3mm(loop: DoStatement): boolean {
  const sub = loop.getAncestor("programUnit");
  if (!sub) return false;
  return sub.code.split("\n")[0].toLowerCase().includes("kernel_3mm");
}

// Collect top-level 2-level pairs: outer whose sole body stmt is a DoStatement
const pairs: { outer: DoStatement; inner: DoStatement }[] = [];
for (const loop of [...Query.search(DoStatement)].filter(isInKernel3mm)) {
  const stmts = loop.body.executableStmts;
  if (stmts.length === 1 && stmts[0] instanceof DoStatement) {
    pairs.push({ outer: loop, inner: stmts[0] as DoStatement });
  }
}
const innerSet = new Set(pairs.map(p => p.inner));
const topPairs = pairs.filter(p => !innerSet.has(p.outer));

console.log(`Found ${topPairs.length} tileable loop pair(s) (tile size = ${T})`);

for (const { outer, inner } of topPairs) {
  const oc = outer.control;
  const ic = inner.control;
  if (!(oc instanceof RangeLoopControl && ic instanceof RangeLoopControl)) continue;

  const ov = oc.var.name;
  const iv = ic.var.name;
  const oLo = oc.lower.code;
  const oHi = oc.upper.code;
  const iLo = ic.lower.code;
  const iHi = ic.upper.code;
  // Tile variables: single-letter loop var repeated gives an implicitly-integer Fortran name
  const ovt = ov.repeat(2);
  const ivt = iv.repeat(2);

  const innerBody = inner.body.executableStmts.map(s => (s as Joinpoint).code);

  const newCode = [
    `do ${ovt} = ${oLo}, ${oHi}, ${T}`,
    `do ${ivt} = ${iLo}, ${iHi}, ${T}`,
    `do ${ov} = ${ovt}, MIN(${ovt} + ${T} - 1, ${oHi})`,
    `do ${iv} = ${ivt}, MIN(${ivt} + ${T} - 1, ${iHi})`,
    ...innerBody,
    `end do`,
    `end do`,
    `end do`,
    `end do`,
  ].join("\n");

  outer.insert("replace", newCode);
  console.log(`  Tiled: (${ov}, ${iv}) -> tile vars (${ovt}, ${ivt})`);
}

console.log("\n=== MODIFIED CODE ===");
console.log((Query.root() as Joinpoint).code);
