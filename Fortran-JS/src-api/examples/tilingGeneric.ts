import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopTilingPass from "../pass/LoopTilingPass.js";
import { Subroutine } from "../Joinpoints.js";

const TILE_SIZE = 32;

const subroutines = Query.search(Subroutine, ($jp) => $jp.moduleName.startsWith('kernel_')).get();

if (subroutines.length === 0) {
  console.log('No kernel_* subroutine found — skipping');
} else {
  for (const sub of subroutines) {
    const result = new LoopTilingPass(TILE_SIZE).apply(sub);
    if (result.appliedPass) {
      console.log(`[tilingGeneric] TILED (tile=${TILE_SIZE}): ${sub.moduleName}`);
    } else {
      console.log(`[tilingGeneric] SKIPPED (no eligible 2-deep perfect nest): ${sub.moduleName}`);
    }
  }
}
