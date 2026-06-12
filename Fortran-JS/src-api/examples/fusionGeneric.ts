import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopFusionPass from "../pass/LoopFusionPass.js";
import { Subroutine } from "../Joinpoints.js";

const subroutines = Query.search(Subroutine, ($jp) => $jp.moduleName.startsWith('kernel_')).get();

if (subroutines.length === 0) {
  console.log('No kernel_* subroutine found — skipping');
} else {
  for (const sub of subroutines) {
    const result = new LoopFusionPass().apply(sub);
    if (result.appliedPass) {
      console.log(`[fusionGeneric] FUSED: ${sub.moduleName}`);
    } else {
      console.log(`[fusionGeneric] SKIPPED (no fusable consecutive loops): ${sub.moduleName}`);
    }
  }
}
