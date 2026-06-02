import Pass from "@specs-feup/lara/api/lara/pass/Pass.js";
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import { DoStatement, Joinpoint } from "../Joinpoints.js";
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
        pairs.push({ outer: loop, inner: stmts[0] as DoStatement });
      }
    }
    const innerSet = new Set(pairs.map(p => p.inner));
    return pairs.filter(p => !innerSet.has(p.outer));
  }
}
