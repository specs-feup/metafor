import Pass from "@specs-feup/lara/api/lara/pass/Pass.js";
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import { DoStatement, Joinpoint } from "../Joinpoints.js";
import loopInterchange, { canInterchange } from "../code/LoopInterchange.js";

/**
 * Pass that interchanges every legal top-level 2-deep perfect loop nest in the subtree.
 *
 * A pair is eligible when:
 * 1. Both loops are range loops.
 * 2. The inner loop's bounds do not reference the outer loop variable (Check 1: no triangular bounds).
 * 3. No nested DO loop inside the inner body has bounds referencing the outer variable (Check 2).
 *
 * All eligible pairs are collected before any transformation so that AST mutations
 * do not interfere with the traversal. In nests deeper than 2, only the outermost
 * 2-level pair is interchanged.
 *
 * @example
 * const pass = new LoopInterchangePass();
 * pass.apply(Query.root());
 */
export default class LoopInterchangePass extends Pass {
  protected _name = "LoopInterchangePass";

  protected _apply_impl($jp: Joinpoint): PassResult {
    const pairs = this._findInterchangeablePairs($jp);
    let appliedPass = false;
    for (const { outer, inner } of pairs) {
      loopInterchange(outer, inner);
      appliedPass = true;
    }
    return new PassResult(this, $jp, { appliedPass, insertedLiteralCode: false });
  }

  protected _findInterchangeablePairs($jp: Joinpoint): { outer: DoStatement; inner: DoStatement }[] {
    // Collect all structural pairs before applying legality checks. Filtering
    // with AST containment keeps independent nests that reuse the same loop
    // variable eligible and avoids relying on proxy object identity.
    const allPairs: { outer: DoStatement; inner: DoStatement }[] = [];
    for (const loop of Query.searchFromInclusive($jp, DoStatement)) {
      const stmts = loop.body.executableStmts;
      if (stmts.length === 1 && stmts[0] instanceof DoStatement) {
        allPairs.push({ outer: loop, inner: stmts[0] as DoStatement });
      }
    }

    const outermostPairs = allPairs.filter(({ outer }, index) =>
      !allPairs.some((candidate, candidateIndex) =>
        candidateIndex !== index && candidate.outer.contains(outer),
      ),
    );

    return outermostPairs.filter(({ outer, inner }) => canInterchange(outer, inner));
  }
}
