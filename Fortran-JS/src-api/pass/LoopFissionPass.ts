import Pass from "@specs-feup/lara/api/lara/pass/Pass.js"
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import { DoStatement, Joinpoint } from "../Joinpoints.js";
import loopFision from "../code/LoopFision.js";

export default class LoopFissionPass extends Pass {
  protected _name = "LoopFissionPass";
  
  protected _apply_impl($jp: Joinpoint): PassResult {
    let appliedPass = false;
    for (const $loop of this._findLoops($jp)) {
      appliedPass = true;
      this._loopFission($loop)
    }

    return new PassResult(this, $jp, {
      appliedPass,
      insertedLiteralCode: true,
    })
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

  protected _loopFission(loop: DoStatement) {
    loopFision(loop)
  }
}