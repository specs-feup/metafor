import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopTilingPass from "../pass/LoopTilingPass.js";
import { Subroutine } from "../Joinpoints.js";

const TILE_SIZE = 32;

const subroutines = Query.search(Subroutine, ($jp) => $jp.moduleName.startsWith('kernel_')).get();

if (subroutines.length === 0) {
  console.log('No kernel_* subroutine found — skipping');
} else {
  for (const sub of subroutines) {
    console.log(`[tilingGeneric] Tiling (tile=${TILE_SIZE}): ${sub.moduleName}`);
    new LoopTilingPass(TILE_SIZE).apply(sub);
  }
}
