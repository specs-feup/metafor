import Query from "@specs-feup/lara/api/weaver/Query.js";
import {
  ArraySubscriptExpr,
  AssignmentStatement,
  DataRef,
  DoStatement,
  Expr,
  Joinpoint,
  RangeLoopControl,
} from "../Joinpoints.js";

export type ArrayAccess = {
  name: string;
};

export type AccessSummary = {
  scalarReads: Set<string>;
  scalarWrites: Set<string>;
  arrayReads: ArrayAccess[];
  arrayWrites: ArrayAccess[];
  hasUnknownStatement: boolean;
};

function emptySummary(): AccessSummary {
  return {
    scalarReads: new Set<string>(),
    scalarWrites: new Set<string>(),
    arrayReads: [],
    arrayWrites: [],
    hasUnknownStatement: false,
  };
}

function mergeSummary(target: AccessSummary, source: AccessSummary): void {
  for (const name of source.scalarReads) target.scalarReads.add(name);
  for (const name of source.scalarWrites) target.scalarWrites.add(name);
  target.arrayReads.push(...source.arrayReads);
  target.arrayWrites.push(...source.arrayWrites);
  target.hasUnknownStatement ||= source.hasUnknownStatement;
}

function arrayNames(accesses: ArrayAccess[]): Set<string> {
  return new Set(accesses.map(({ name }) => name));
}

function intersects(left: Set<string>, right: Set<string>): boolean {
  for (const value of left) {
    if (right.has(value)) return true;
  }
  return false;
}

/**
 * Returns the scalar references in an expression. The data reference that is
 * the base of an array access is excluded; its subscripts remain references.
 */
function scalarReferences(expression: Expr): Set<string> {
  const arrays = [...Query.searchFromInclusive(expression, ArraySubscriptExpr)];
  const arrayBases = arrays.map((array) => array.var);
  const result = new Set<string>();

  for (const reference of Query.searchFromInclusive(expression, DataRef)) {
    if (reference instanceof ArraySubscriptExpr) continue;
    if (arrayBases.some((base) => base.equals(reference))) continue;
    result.add(reference.name);
  }

  return result;
}

function arrayReads(expression: Expr): ArrayAccess[] {
  return [...Query.searchFromInclusive(expression, ArraySubscriptExpr)].map(
    (array) => ({ name: array.var.name }),
  );
}

function summarizeAssignment(statement: AssignmentStatement): AccessSummary {
  const result = emptySummary();
  const variable = statement.variable;

  if (variable instanceof ArraySubscriptExpr) {
    result.arrayWrites.push({ name: variable.var.name });
    for (const name of scalarReferences(variable)) {
      result.scalarReads.add(name);
    }
  } else {
    result.scalarWrites.add(variable.name);
  }

  for (const name of scalarReferences(statement.expr)) {
    result.scalarReads.add(name);
  }
  result.arrayReads.push(...arrayReads(statement.expr));

  return result;
}

function summarizeStatement(statement: Joinpoint): AccessSummary {
  if (statement instanceof AssignmentStatement) {
    return summarizeAssignment(statement);
  }

  if (statement instanceof DoStatement) {
    const control = statement.control;
    if (!(control instanceof RangeLoopControl)) {
      return { ...emptySummary(), hasUnknownStatement: true };
    }

    const result = summarizeStatements(statement.body.executableStmts);
    for (const expression of controlExpressions(control)) {
      for (const name of scalarReferences(expression)) {
        result.scalarReads.add(name);
      }
    }
    return result;
  }

  return { ...emptySummary(), hasUnknownStatement: true };
}

export function controlExpressions(control: RangeLoopControl): Expr[] {
  const expressions = [control.lower, control.upper];
  if (control.step !== undefined) expressions.push(control.step);
  return expressions;
}

export function summarizeStatements(
  statements: readonly Joinpoint[],
): AccessSummary {
  const result = emptySummary();
  for (const statement of statements) {
    mergeSummary(result, summarizeStatement(statement));
  }
  return result;
}

export function summarizeLoop(loop: DoStatement): AccessSummary {
  return summarizeStatements(loop.body.executableStmts);
}

/**
 * Returns whether moving the two statement groups into one loop could change
 * a dependency. The analysis deliberately rejects any shared array access
 * involving a write because this API has no affine dependence solver.
 */
export function hasCrossGroupDependency(
  first: AccessSummary,
  second: AccessSummary,
): boolean {
  if (
    intersects(first.scalarWrites, second.scalarReads) ||
    intersects(first.scalarWrites, second.scalarWrites) ||
    intersects(second.scalarWrites, first.scalarReads)
  ) {
    return true;
  }

  const firstReadArrays = arrayNames(first.arrayReads);
  const firstWriteArrays = arrayNames(first.arrayWrites);
  const secondReadArrays = arrayNames(second.arrayReads);
  const secondWriteArrays = arrayNames(second.arrayWrites);

  return (
    intersects(firstWriteArrays, secondReadArrays) ||
    intersects(firstWriteArrays, secondWriteArrays) ||
    intersects(secondWriteArrays, firstReadArrays)
  );
}

/**
 * Returns whether a loop body contains a dependence that makes loop-order
 * transformations unsafe. Unknown statements are rejected conservatively.
 */
export function hasLoopCarriedDependency(summary: AccessSummary): boolean {
  if (summary.hasUnknownStatement) return true;

  if (intersects(summary.scalarWrites, summary.scalarReads)) return true;

  const reads = arrayNames(summary.arrayReads);
  const writes = arrayNames(summary.arrayWrites);
  return intersects(reads, writes);
}

export function referencesName(expression: Expr, name: string): boolean {
  for (const reference of Query.searchFromInclusive(expression, DataRef)) {
    if (reference instanceof ArraySubscriptExpr) {
      if (reference.var.name === name) return true;
    } else if (reference.name === name) {
      return true;
    }
  }
  return false;
}
