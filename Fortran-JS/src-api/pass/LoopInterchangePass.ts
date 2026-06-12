import Pass from "@specs-feup/lara/api/lara/pass/Pass.js";
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import { DoStatement, Joinpoint, RangeLoopControl } from "../Joinpoints.js";
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
    // Collect all structural 2-deep perfect nests regardless of legality.
    // innerSet must be built from ALL pairs so that nests deeper than 2 are
    // handled correctly: an inner pair nested inside an illegal outer pair must
    // not be interchanged either (its "outer" loop is still the inner of an
    // unprocessed pair and evaluation-order constraints may still apply).
    const allPairs: { outer: DoStatement; inner: DoStatement }[] = [];
    for (const loop of Query.searchFrom($jp, DoStatement)) {
      const stmts = loop.body.executableStmts;
      if (stmts.length === 1 && stmts[0] instanceof DoStatement) {
        allPairs.push({ outer: loop, inner: stmts[0] as DoStatement });
      }
    }
    // LARA wraps each AST node in a new JS proxy on every access, so JS object
    // identity cannot be used to detect that the same loop appears as both an
    // outer in one pair and an inner in another. Key by loop variable name
    // instead — unique per subroutine for PolyBench's affine loops.
    const innerVarNames = new Set(
      allPairs
        .map(p => p.inner.control)
        .filter((c): c is RangeLoopControl => c instanceof RangeLoopControl)
        .map(c => c.var.name)
    );
    return allPairs
      .filter(({ outer }) => {
        const oc = outer.control;
        return !(oc instanceof RangeLoopControl && innerVarNames.has(oc.var.name));
      })
      .filter(({ outer, inner }) => canInterchange(outer, inner));
  }
}
