import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopTilingPass from "../pass/LoopTilingPass.js";
import { Subroutine } from "../Joinpoints.js";

const TILE_SIZE = 32;

const targetSubroutine = Query.search(Subroutine, { moduleName: 'kernel_3mm' }).getFirst();

if (targetSubroutine) {
  console.log(`Found ${targetSubroutine.moduleName}, tiling loops with tile size ${TILE_SIZE}`);
  new LoopTilingPass(TILE_SIZE).apply(targetSubroutine);
} else {
  console.log('Target subroutine not found');
}
