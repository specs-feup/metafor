# fortran-transpiler

Fortran source-to-source compiler based on the LARA framework.

## Previous Work

This compiler will be heavily inspired by Clava, a C/C++ source-to-source compiler that is also based on the LARA framework.

Clava already supports the following transformations:

- [Automatic insertion of OpenMP pragmas](https://github.com/specs-feup/clava/blob/master/ClavaLaraApi/src-lara-clava/clava/clava/autopar/Parallelize.lara)
- [Function inlining](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/opt/Inlining.ts)
- [Function outlining](https://github.com/tiagolascasas/clava-code-transforms/blob/master/src/Outliner.ts)
- [Array flattening](https://github.com/tiagolascasas/clava-code-transforms/blob/master/src/ArrayFlattener.ts)
- [Constant folding and propagation](https://github.com/tiagolascasas/clava-code-transforms/blob/master/src/constfolding/FoldingPropagationCombiner.ts)
- [Struct decomposition](https://github.com/tiagolascasas/clava-code-transforms/blob/master/src/StructDecomposer.ts)
- [Function voidification](https://github.com/tiagolascasas/clava-code-transforms/blob/master/src/Voidifier.ts)
- [Normalizing code](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/opt/NormalizeToSubset.ts) to a subset of the language, including:
  - [Decomposition of complex statements into several, simpler statements](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/code/StatementDecomposer.ts)
  - [Converting static local variables to static global variables](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/pass/LocalStaticToGlobal.ts)
- [Conversion of switch statements to ifs](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/pass/TransformSwitchToIf.ts)
- Loop conversion ([for to while](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/code/ForToWhileStmt.ts), [do to while](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/code/DoToWhileStmt.ts))
- [Ensure there is a single return in a function](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/pass/SingleReturnFunction.ts)
- [Remove variable shadowing](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/code/RemoveShadowing.ts)
- [Simplify ternary operator](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/code/SimplifyTernaryOp.ts)
- [Simplify compound assignments](https://github.com/specs-feup/clava/blob/master/Clava-JS/src-api/clava/code/SimplifyAssignment.ts)

## Roadmap

The next steps for the Fortran source-to-source compiler are as follows:

- Developing an AST-based IR to represent Fortran that is capable of generating Fortran code back from the IR [Java]
- Dumper based on the Flang parser capable of outputting the necessary information to reconstruct an IR of the source code [C++]
- Base API [TypeScript]
- Library for Fortran transformations, which should include  [TypeScript]:
  - Loop unrolling
  - Loop tiling
  - Loop interchange
  - Loop fission
  - Loop fusion
  - Loop-invariant code motion
  - Loop peeling
  - Loop strip-mining
  - OpenMP directive transformations. These tranformations should transform code with OpenMP directives not-yet supported by Flang into code with supported directives. The particular transformations to target are dependent of inputs from partners
  
