import Query from "@specs-feup/lara/api/weaver/Query.js";
import { DoStatement, RangeLoopControl, Joinpoint } from "./Joinpoints.js";

const FACTOR = 4;

function isInnermostLoop(loop: DoStatement): boolean {
  for (const desc of loop.descendants) {
    if (desc instanceof DoStatement) return false;
  }
  return true;
}

// Replace standalone occurrences of varName with varName+offset.
// Uses word-boundary regex so "k" matches but "nk" or "ki" do not.
function substituteVar(code: string, varName: string, offset: number): string {
  if (offset === 0) return code;
  return code.replace(new RegExp(`\\b${varName}\\b`, "g"), `${varName}+${offset}`);
}

function isInKernel3mm(loop: DoStatement): boolean {
  const sub = loop.getAncestor("programUnit");
  if (!sub) return false;
  return sub.code.split("\n")[0].toLowerCase().includes("kernel_3mm");
}

// TODO: 
// divide it to searching and modification functions
// search from the top program -> then loops

// Collect all innermost loops before making any modifications
const innermostLoops = [...Query.search(DoStatement)]
  .filter(isInKernel3mm)
  .filter(isInnermostLoop);
console.log(`Found ${innermostLoops.length} innermost loop(s), unrolling by factor ${FACTOR}`);

for (const loop of innermostLoops) {
  const ctrl = loop.control;
  if (!(ctrl instanceof RangeLoopControl)) {
    console.log(`  Skipping loop with non-range control`);
    continue;
  }

  const v = ctrl.var.name;
  const lo = ctrl.lower.code;
  const hi = ctrl.upper.code;
  const bodyLines: string[] = loop.body.executableStmts.map(s => (s as Joinpoint).code);

  console.log(`  Unrolling: do ${v} = ${lo}, ${hi}  (${bodyLines.length} body stmt(s))`);

  // For each unroll offset, replicate the full body with the loop variable substituted.
  // Keeping body statements together per offset preserves intra-iteration ordering.
  const mainBody = Array.from({ length: FACTOR }, (_, off) =>
    bodyLines.map(line => substituteVar(line, v, off))
  ).flat();

  const mainLoop = [
    `do ${v} = ${lo}, ${hi} - ${FACTOR - 1}, ${FACTOR}`,
    ...mainBody,
    `end do`,
  ].join("\n");

  // Cleanup loop handles the (< FACTOR) remaining iterations
  const cleanupLoop = [
    `do ${v} = ${hi} - MOD(${hi}, ${FACTOR}) + 1, ${hi}`,
    ...bodyLines,
    `end do`,
  ].join("\n");

  loop.insert("replace", `${mainLoop}\n${cleanupLoop}`);
}

console.log("\n=== MODIFIED CODE ===");
console.log((Query.root() as Joinpoint).code);
