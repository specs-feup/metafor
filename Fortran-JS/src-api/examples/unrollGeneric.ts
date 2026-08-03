import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopUnrollPass from "../pass/LoopUnrollPass.js";
import { Subroutine } from "../Joinpoints.js";

const FACTOR = 4;

const subroutines = Query.search(Subroutine, ($jp) => $jp.moduleName.startsWith('kernel_')).get();

if (subroutines.length === 0) {
  console.log('No kernel_* subroutine found — skipping');
} else {
  for (const sub of subroutines) {
    const result = new LoopUnrollPass(FACTOR).apply(sub);
    if (result.appliedPass) {
      console.log(`[unrollGeneric] UNROLLED (factor=${FACTOR}): ${sub.moduleName}`);
    } else {
      console.log(`[unrollGeneric] SKIPPED (no unrollable innermost loops): ${sub.moduleName}`);
    }
  }
}
