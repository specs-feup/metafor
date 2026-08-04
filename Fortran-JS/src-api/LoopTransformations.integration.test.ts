import { readFileSync } from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { registerSourceCodes } from "@specs-feup/lara/jest/jestHelpers.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import LoopInterchangePass from "./pass/LoopInterchangePass.js";
import LoopTilingPass from "./pass/LoopTilingPass.js";
import LoopUnrollPass from "./pass/LoopUnrollPass.js";
import { canInterchange } from "./code/LoopInterchange.js";
import {
  DoStatement,
  Joinpoint,
  RangeLoopControl,
  Subroutine,
} from "./Joinpoints.js";

const fixturePath = path.resolve(
  path.dirname(fileURLToPath(import.meta.url)),
  "../../FortranParser/resources-test/fortran/parser/polybench/3mm.json",
);

describe("loop transformation integration", () => {
  registerSourceCodes({ "3mm.json": readFileSync(fixturePath, "utf8") });

  it("builds AST expressions while unrolling innermost loops", () => {
    const root = Query.root() as Joinpoint;

    const result = new LoopUnrollPass(4).apply(root);

    expect(result.appliedPass).toBe(true);
    expect(root.code).toContain("MOD(");
  });

  it("uses fresh names when tiling legal nests", () => {
    const root = Query.root() as Joinpoint;

    const result = new LoopTilingPass(4).apply(root);

    expect(result.appliedPass).toBe(true);
    expect(root.code).toContain("_tile");
  });

  it("interchanges legal nests without crashing on dependent nests", () => {
    const root = Query.root() as Joinpoint;

    const result = new LoopInterchangePass().apply(root);

    expect(result.appliedPass).toBe(true);
    expect(root.code).toContain("DO");
  });

  it("rejects the loop-carried dependency in the matrix multiply kernel", () => {
    const kernel = Query.search(Subroutine, { moduleName: "kernel_3mm" }).getFirst();
    expect(kernel).toBeDefined();
    if (kernel === undefined) return;

    const pair = [...Query.searchFrom(kernel, DoStatement)].find((outer) => {
      const outerControl = outer.control;
      const body = outer.body.executableStmts;
      const inner = body[0];

      return outerControl instanceof RangeLoopControl
        && outerControl.var.name === "i"
        && body.length === 1
        && inner instanceof DoStatement
        && inner.control instanceof RangeLoopControl
        && inner.control.var.name === "j";
    });

    expect(pair).toBeDefined();
    if (pair === undefined) return;

    expect(canInterchange(pair, pair.body.executableStmts[0] as DoStatement)).toBe(false);
  });
});
