import Pass from "@specs-feup/lara/api/lara/pass/Pass.js";
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import { LaraJoinPoint } from "@specs-feup/lara/api/LaraJoinPoint.js";
import { DoStatement, Joinpoint } from "../Joinpoints.js";

export default class LoopFusionPass extends Pass {
  protected _name = 'LoopFusionPass';

  protected _apply_impl($jp: Joinpoint): PassResult {
    throw new Error("Method not implemented.");
  }

    protected *_findLoops($jp: Joinpoint): Generator<DoStatement> {
      for (const child of $jp.children) {
        yield* this._findLoops(child);
      }
      if (
        $jp instanceof DoStatement && $jp.body.executableStmts.length > 1
      ) {
        yield $jp;
      }
    }
}