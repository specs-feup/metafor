import Pass from "@specs-feup/lara/api/lara/pass/Pass.js";
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import { DoStatement, Joinpoint } from "../Joinpoints.js";
import loopUnroll, { canUnroll } from "../code/LoopUnroll.js";

/**
 * Pass that unrolls every innermost range do-loop in the subtree by a given
 * factor (loop unrolling), applied recursively.
 *
 * A loop is "innermost" when none of its descendants is also a do-loop.
 * All eligible loops are collected before any transformation so that AST
 * mutations do not interfere with the traversal.
 *
 * @example
 * const pass = new LoopUnrollPass(4);
 * pass.apply(Query.root());
 */
export default class LoopUnrollPass extends Pass {
  protected _name = "LoopUnrollPass";

  constructor(private readonly factor: number = 4) {
    super();
  }

  protected _apply_impl($jp: Joinpoint): PassResult {
    const loops = [...this._findInnermostLoops($jp)];
    let appliedPass = false;
    for (const $loop of loops) {
      loopUnroll($loop, this.factor);
      appliedPass = true;
    }
    return new PassResult(this, $jp, { appliedPass, insertedLiteralCode: true });
  }

  /**
   * Yields every innermost do-loop in the subtree rooted at `$jp`.
   *
   * A loop is innermost when none of its descendants is a do-loop.
   * Recursion does not descend into innermost loops.
   */
  protected *_findInnermostLoops($jp: Joinpoint): Generator<DoStatement> {
    if ($jp instanceof DoStatement) {
      const hasNestedLoop = $jp.descendants.some(d => d instanceof DoStatement);
      if (!hasNestedLoop && canUnroll($jp, this.factor)) {
        yield $jp;
        return;
      }
    }
    for (const child of [...$jp.children]) {
      yield* this._findInnermostLoops(child);
    }
  }
}
