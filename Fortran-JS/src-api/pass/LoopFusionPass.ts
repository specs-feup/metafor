import Pass from "@specs-feup/lara/api/lara/pass/Pass.js";
import PassResult from "@specs-feup/lara/api/lara/pass/results/PassResult.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import { ArraySubscriptExpr, AssignmentStatement, DoStatement, Joinpoint, RangeLoopControl } from "../Joinpoints.js";
import loopFusion from "../code/LoopFusion.js";

type ArrayAccess = { arrayName: string; subCodes: string[] };

function loopWrites(loop: DoStatement): ArrayAccess[] {
  const result: ArrayAccess[] = [];
  for (const stmt of Query.searchFrom(loop.body, AssignmentStatement)) {
    const lhs = stmt.variable;
    if (lhs instanceof ArraySubscriptExpr) {
      // .name on ArraySubscriptExpr is ""; the array identifier is at .var.name
      result.push({ arrayName: lhs.var.name, subCodes: lhs.subscripts.map(s => s.code) });
    }
  }
  return result;
}

function loopReads(loop: DoStatement): ArrayAccess[] {
  const result: ArrayAccess[] = [];
  for (const stmt of Query.searchFrom(loop.body, AssignmentStatement)) {
    for (const expr of Query.searchFromInclusive(stmt.expr, ArraySubscriptExpr)) {
      result.push({ arrayName: expr.var.name, subCodes: expr.subscripts.map(s => s.code) });
    }
  }
  return result;
}

function innerLoopVars(loop: DoStatement): Set<string> {
  const vars = new Set<string>();
  for (const nested of Query.searchFrom(loop.body, DoStatement)) {
    if (nested.kind === 'range') {
      const ctl = nested.control;
      if (ctl instanceof RangeLoopControl) vars.add(ctl.var.name);
    }
  }
  return vars;
}

function hasVar(code: string, varName: string): boolean {
  return new RegExp(`\\b${varName}\\b`).test(code);
}

function hasAnyVar(code: string, vars: Set<string>): boolean {
  return [...vars].some(v => hasVar(code, v));
}

/**
 * Pass that fuses consecutive range do-loops with identical boundaries into a
 * single loop (loop fusion), applied recursively across the subtree.
 *
 * At each scope level, adjacent do-loops that share the same range control are
 * grouped and fused into the first loop of each group. Groups are split at any
 * pair that fails a legality check:
 *
 * - Check A: a write in loop A has a subscript that does not contain the fusion
 *   variable (the same element is written/accumulated on every iteration) and the
 *   same array is read in loop B — the reader would see a partial value.
 *
 * - Check B: array X is written in loop A as `X(inner, fusion_var)` (column-major)
 *   and read in loop B as `X(fusion_var, inner)` (row-major) — a cross-iteration
 *   transposed dependency.
 *
 * - Check C: array X is written in one loop with a subscript that contains the
 *   fusion variable but no inner loop variable (one element per fusion iteration),
 *   and read in the other loop with a subscript that contains an inner loop
 *   variable (reads across the full range) — a forward or backward cross-iteration
 *   dependency.
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
    let children: Joinpoint[];
    try { children = [...$jp.children]; } catch (_) { children = []; }
    for (const child of children) {
      yield* this._findAllFusableSets(child);
    }
    for (const set of this._findFusableSets($jp)) {
      yield set;
    }
  }

  /**
   * Returns groups of consecutive range do-loops among the direct children of
   * `$jp` that share identical loop boundaries and pass all three legality
   * checks. Groups are split whenever a consecutive pair is illegal; only
   * groups of length ≥ 2 are returned.
   */
  protected _findFusableSets($jp: Joinpoint): DoStatement[][] {
    const result: DoStatement[][] = [];
    let group: DoStatement[] = [];

    let children: Joinpoint[];
    try { children = [...$jp.children]; } catch (_) { return result; }

    for (const child of children) {
      if (child instanceof DoStatement && child.kind === 'range') {
        if (group.length === 0) {
          group = [child];
        } else if (group[0].sameScope(child)) {
          if (this._canFusePair(group[group.length - 1], child)) {
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

  /**
   * Returns false if fusing `a` immediately before `b` would violate one of
   * three dependency patterns detectable from the AST.
   */
  protected _canFusePair(a: DoStatement, b: DoStatement): boolean {
    const fv = (a.control as RangeLoopControl).var.name;
    const ivA = innerLoopVars(a);
    const ivB = innerLoopVars(b);

    const writesA = loopWrites(a);
    const writesB = loopWrites(b);
    const readsA  = loopReads(a);
    const readsB  = loopReads(b);

    const readNamesA = new Set(readsA.map(r => r.arrayName));
    const readNamesB = new Set(readsB.map(r => r.arrayName));

    // Check A: write subscript contains no instance of the fusion variable →
    // the same array element is modified on every fusion iteration.
    // If the other loop reads that element, it will see an incomplete value.
    for (const w of writesA) {
      if (!w.subCodes.some(sc => hasVar(sc, fv)) && readNamesB.has(w.arrayName)) return false;
    }
    for (const w of writesB) {
      if (!w.subCodes.some(sc => hasVar(sc, fv)) && readNamesA.has(w.arrayName)) return false;
    }

    // Check B: array X written as X(inner_var, fv) in one loop and read as
    // X(fv, inner_var) in the other — transposed cross-iteration dependency.
    const checkTransposed = (
      writes: ArrayAccess[], reads: ArrayAccess[],
      writeIV: Set<string>, readIV: Set<string>
    ) => {
      for (const w of writes) {
        if (w.subCodes.length !== 2) continue;
        const [ws0, ws1] = w.subCodes;
        if (!hasAnyVar(ws0, writeIV) || !hasVar(ws1, fv)) continue;
        for (const r of reads) {
          if (r.arrayName !== w.arrayName || r.subCodes.length !== 2) continue;
          const [rs0, rs1] = r.subCodes;
          if (hasVar(rs0, fv) && hasAnyVar(rs1, readIV)) return true;
        }
      }
      return false;
    };
    if (checkTransposed(writesA, readsB, ivA, ivB)) return false;
    if (checkTransposed(writesB, readsA, ivB, ivA)) return false;

    // Check C: array X written with a subscript that contains the fusion
    // variable but no inner loop variable (one element per fusion iteration),
    // and read by the other loop with a subscript that contains an inner loop
    // variable (reads across the full range within one fusion iteration).
    const checkOnePerIter = (
      writes: ArrayAccess[], reads: ArrayAccess[],
      writeIV: Set<string>, readIV: Set<string>
    ) => {
      for (const w of writes) {
        const allSubs = w.subCodes.join(' ');
        if (!hasVar(allSubs, fv) || hasAnyVar(allSubs, writeIV)) continue;
        for (const r of reads) {
          if (r.arrayName !== w.arrayName) continue;
          if (hasAnyVar(r.subCodes.join(' '), readIV)) return true;
        }
      }
      return false;
    };
    if (checkOnePerIter(writesA, readsB, ivA, ivB)) return false;
    if (checkOnePerIter(writesB, readsA, ivB, ivA)) return false;

    return true;
  }
}
