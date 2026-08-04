import { readFileSync } from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { registerSourceCodes } from "@specs-feup/lara/jest/jestHelpers.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import FortranJoinPoints from "./FortranJoinPoints.js";
import { canFuse } from "./code/LoopFusion.js";
import { canInterchange } from "./code/LoopInterchange.js";
import { canTile } from "./code/LoopTiling.js";
import LoopFissionPass from "./pass/LoopFissionPass.js";
import LoopFusionPass from "./pass/LoopFusionPass.js";
import LoopUnrollPass from "./pass/LoopUnrollPass.js";
import { DoStatement, Joinpoint, RangeLoopControl } from "./Joinpoints.js";

const testDirectory = path.dirname(fileURLToPath(import.meta.url));
const parserFixtures = path.resolve(testDirectory, "../../FortranParser/resources-test/fortran/parser");

const sourceCodes = {
  "do.json": readFileSync(path.join(parserFixtures, "do.json"), "utf8"),
  "element_access.json": readFileSync(
    path.join(parserFixtures, "arrays/element_access.json"),
    "utf8",
  ),
};

describe("loop transformations", () => {
  registerSourceCodes(sourceCodes);

  it("traverses declaration and initialization joinpoints", () => {
    const root = Query.root() as Joinpoint;

    expect(() => root.descendants).not.toThrow();
    expect([...Query.search(DoStatement)]).toHaveLength(3);
  });

  it("does not fission statements with a scalar dependency", () => {
    const root = Query.root() as Joinpoint;
    const before = root.code;

    const result = new LoopFissionPass().apply(root);

    expect(result.appliedPass).toBe(false);
    expect(root.code).toBe(before);
  });

  it("does not unroll loops with an explicit step", () => {
    const root = Query.root() as Joinpoint;
    const before = root.code;

    const result = new LoopUnrollPass(4).apply(root);

    expect(result.appliedPass).toBe(false);
    expect(root.code).toBe(before);
  });

  it("checks loop-control steps through the AST", () => {
    const outerControl = FortranJoinPoints.rangeLoopControl(
      FortranJoinPoints.dataRef("i"),
      FortranJoinPoints.intLiteral(1),
      FortranJoinPoints.dataRef("n"),
    );
    const innerControl = FortranJoinPoints.rangeLoopControl(
      FortranJoinPoints.dataRef("j"),
      FortranJoinPoints.intLiteral(1),
      FortranJoinPoints.dataRef("m"),
    );
    innerControl.setStep(FortranJoinPoints.dataRef("i"));

    const outer = FortranJoinPoints.doStatement(outerControl);
    const inner = FortranJoinPoints.doStatement(innerControl);
    outer.body.insertEnd(inner);

    expect(canInterchange(outer, inner)).toBe(false);
    expect(canTile(outer, inner)).toBe(false);
    expect(outer.control).toBeInstanceOf(RangeLoopControl);
  });

  it("does not fuse loops from different parent scopes", () => {
    const first = FortranJoinPoints.doStatement(
      FortranJoinPoints.rangeLoopControl(
        FortranJoinPoints.dataRef("i"),
        FortranJoinPoints.intLiteral(1),
        FortranJoinPoints.dataRef("n"),
      ),
    );
    const second = FortranJoinPoints.doStatement(
      FortranJoinPoints.rangeLoopControl(
        FortranJoinPoints.dataRef("i"),
        FortranJoinPoints.intLiteral(1),
        FortranJoinPoints.dataRef("n"),
      ),
    );

    FortranJoinPoints.execution([first]);
    FortranJoinPoints.execution([second]);

    expect(canFuse([first, second])).toBe(false);
  });

  it("fuses adjacent loops with the same range in one execution scope", () => {
    const first = FortranJoinPoints.doStatement(
      FortranJoinPoints.rangeLoopControl(
        FortranJoinPoints.dataRef("i"),
        FortranJoinPoints.intLiteral(1),
        FortranJoinPoints.dataRef("n"),
      ),
    );
    const second = FortranJoinPoints.doStatement(
      FortranJoinPoints.rangeLoopControl(
        FortranJoinPoints.dataRef("i"),
        FortranJoinPoints.intLiteral(1),
        FortranJoinPoints.dataRef("n"),
      ),
    );
    const scope = FortranJoinPoints.execution([first, second]);

    const result = new LoopFusionPass().apply(scope);

    expect(result.appliedPass).toBe(true);
    expect(scope.executableStmts).toHaveLength(1);
  });
});
