import Pass from "@specs-feup/lara/api/lara/pass/Pass.js";
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import { DataRef, DoStatement, Joinpoint, RangeLoopControl } from "../Joinpoints.js";
import loopTile, { canTile } from "../code/LoopTiling.js";

/**
 * Pass that tiles every top-level 2-deep perfect loop nest in the subtree.
 *
 * A tileable pair is an outer loop whose sole body statement is another loop.
 * All eligible pairs are collected before any transformation so that AST
 * mutations do not interfere with the traversal. In nests deeper than 2,
 * only the outermost 2-level pair is tiled.
 *
 * @example
 * const pass = new LoopTilingPass(32);
 * pass.apply(Query.root());
 */
export default class LoopTilingPass extends Pass {
  protected _name = "LoopTilingPass";

  constructor(private readonly tileSize: number = 32) {
    super();
  }

  protected _apply_impl($jp: Joinpoint): PassResult {
    const pairs = this._findTileablePairs($jp);
    let appliedPass = false;
    for (const { outer, inner } of pairs) {
      loopTile(outer, inner, this.tileSize);
      appliedPass = true;
    }
    return new PassResult(this, $jp, { appliedPass, insertedLiteralCode: false });
  }

  protected _findTileablePairs($jp: Joinpoint): { outer: DoStatement; inner: DoStatement }[] {
    const pairs: { outer: DoStatement; inner: DoStatement }[] = [];
    for (const loop of Query.searchFrom($jp, DoStatement)) {
      const stmts = loop.body.executableStmts;
      if (stmts.length === 1 && stmts[0] instanceof DoStatement
          && canTile(loop, stmts[0] as DoStatement)) {

        const outer = loop;
        const inner = stmts[0] as DoStatement;
        const outerVarName = (outer.control as RangeLoopControl).var.name;

        // Legality check: reject pairs where any loop in inner's subtree has
        // bounds referencing the outer loop variable. This catches triangular
        // loops like `do k = 1, i - 1` (trmm) and `do i = j, n` (reg_detect),
        // where tiling the outer loop changes which iterations are valid for
        // the dependent inner/descendant loop, producing wrong results.
        // Try/catch guards against null-node traversal errors on staging branch.
        let illegal = false;
        try {
          const subtreeLoops = [inner, ...Query.searchFrom(inner, DoStatement).get()];
          illegal = subtreeLoops.some(subLoop => {
            if (!(subLoop.control instanceof RangeLoopControl)) return false;
            try {
              return Query.searchFrom(subLoop.control, DataRef, { name: outerVarName }).get().length > 0;
            } catch (_) { return false; }
          });
        } catch (_) { /* skip on traversal errors */ }

        if (!illegal) {
          pairs.push({ outer, inner });
        }
      }
    }
    const innerSet = new Set(pairs.map(p => p.inner));
    return pairs.filter(p => !innerSet.has(p.outer));
  }
}
