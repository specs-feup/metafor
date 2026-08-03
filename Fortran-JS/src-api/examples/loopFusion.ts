import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopFusionPass from "../pass/LoopFusionPass.js";
import { Subroutine } from "../Joinpoints.js";



// Collect loops with more than one body statement before any modifications
const targetSubroutine = Query.search(Subroutine, ($jp) => $jp.moduleName === 'kernel_3mm').getFirst()


if (targetSubroutine) {
  console.log(`Found ${targetSubroutine.moduleName} loop(s) to fission`);

  const loopFusionPass = new LoopFusionPass()
  
  loopFusionPass.apply(targetSubroutine)
} else {
  console.log(`Finish: targetSubroutine has not been found`);
}
