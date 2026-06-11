import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopFusionPass from "../pass/LoopFusionPass.js";
import { Subroutine } from "../Joinpoints.js";

const subroutines = Query.search(Subroutine, ($jp) => $jp.moduleName.startsWith('kernel_')).get();

if (subroutines.length === 0) {
  console.log('No kernel_* subroutine found — skipping');
} else {
  for (const sub of subroutines) {
    console.log(`[fusionGeneric] Applying loop fusion: ${sub.moduleName}`);
    new LoopFusionPass().apply(sub);
  }
}
