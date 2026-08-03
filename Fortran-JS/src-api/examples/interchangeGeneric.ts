import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopInterchangePass from "../pass/LoopInterchangePass.js";
import { Subroutine } from "../Joinpoints.js";

const subroutines = Query.search(Subroutine, ($jp) => $jp.moduleName.startsWith('kernel_')).get();

if (subroutines.length === 0) {
  console.log('No kernel_* subroutine found — skipping');
} else {
  for (const sub of subroutines) {
    const result = new LoopInterchangePass().apply(sub);
    if (result.appliedPass) {
      console.log(`[interchangeGeneric] INTERCHANGED: ${sub.moduleName}`);
    } else {
      console.log(`[interchangeGeneric] SKIPPED (no eligible/legal 2-deep perfect nest): ${sub.moduleName}`);
    }
  }
}
