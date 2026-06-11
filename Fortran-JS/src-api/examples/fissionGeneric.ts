import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopFissionPass from "../pass/LoopFissionPass.js";
import { Subroutine } from "../Joinpoints.js";

const subroutines = Query.search(Subroutine, ($jp) => $jp.moduleName.startsWith('kernel_')).get();

if (subroutines.length === 0) {
  console.log('No kernel_* subroutine found — skipping');
} else {
  for (const sub of subroutines) {
    console.log(`[fissionGeneric] Applying loop fission: ${sub.moduleName}`);
    new LoopFissionPass().apply(sub);
  }
}
