import Pass from "@specs-feup/lara/api/lara/pass/Pass.js";
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import { DoStatement, Joinpoint } from "../Joinpoints.js";
import loopFusion, { canFusePair } from "../code/LoopFusion.js";

/**
 * Pass that fuses consecutive range do-loops with identical boundaries into a
 * single loop (loop fusion), applied recursively across the subtree.
 *
 * At each scope level, adjacent do-loops that share the same range control are
 * grouped and fused into the first loop of each group. Groups are split at any
 * pair with a dependency detected by the AST access summary. The legality
 * analysis is deliberately conservative because this API does not yet perform
 * affine dependence tests.
 *
 * @example
 * const pass = new LoopFusionPass();
 * pass.apply(Query.root());
 */
export default class LoopFusionPass extends Pass {
  protected _name = "LoopFusionPass";

  protected _apply_impl($jp: Joinpoint): PassResult {
    const allSets = [...this._findAllFusableSets($jp)];
    let appliedPass = false;
    for (const set of allSets) {
      loopFusion(set);
      appliedPass = true;
    }
    return new PassResult(this, $jp, { appliedPass, insertedLiteralCode: false });
  }

  /**
   * Yields all groups of fusable loops in the subtree (post-order), so that
   * inner loops are fused before their containing scope is inspected.
   */
  protected *_findAllFusableSets($jp: Joinpoint): Generator<DoStatement[]> {
    for (const child of $jp.children) {
      yield* this._findAllFusableSets(child);
    }
    for (const set of this._findFusableSets($jp)) {
      yield set;
    }
  }

  /**
   * Returns groups of consecutive range do-loops among the direct children of
   * `$jp` that share identical loop boundaries and pass the legality check.
   * Groups are split whenever a consecutive pair is illegal; only groups of
   * length at least two are returned.
   */
  protected _findFusableSets($jp: Joinpoint): DoStatement[][] {
    const result: DoStatement[][] = [];
    let group: DoStatement[] = [];

    for (const child of $jp.children) {
      if (child instanceof DoStatement && child.kind === "range") {
        if (group.length === 0) {
          group = [child];
        } else if (group[0].sameScope(child)) {
          if (group.every((existing) => canFusePair(existing, child))) {
            group.push(child);
          } else {
            if (group.length >= 2) result.push(group);
            group = [child];
          }
        } else {
          if (group.length >= 2) result.push(group);
          group = [child];
        }
      } else {
        if (group.length >= 2) result.push(group);
        group = [];
      }
    }
    if (group.length >= 2) result.push(group);
    return result;
  }
}
