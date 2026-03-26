# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

**Java modules** (FortranAst, FortranParser, FortranWeaver) — each module has its own Gradle build:
```sh
# Run all tests for a module
cd FortranParser && ./gradlew test

# Run a single test by name (JUnit 5)
cd FortranParser && ./gradlew test --tests "pt.up.fe.specs.fortran.parser.FortranParserTest.testArrayImpliedDo"

# Build a module
cd FortranParser && ./gradlew build
```

**TypeScript CLI** (Fortran-JS):
```sh
cd Fortran-JS
npm install
npm run build-interfaces  # regenerate TS joinpoints from FortranWeaver JSON
npm run build          # tsc -b src-api src-code
npm test               # Jest with --experimental-vm-modules
```

## Architecture

**Pipeline**: Fortran source → Flang compiler (external) dumps JSON → `FortranJsonParser` → `FortranAstBuilder` → `FortranNode` tree → `getCode()` regenerates Fortran → TypeScript API (Fortran-JS/metafor)

### Module Responsibilities

- **FortranAst** — AST node class definitions. Each node extends `FortranNode` (which extends `DataNode<FortranNode>` from jOptions). Nodes are organized under `nodes/`: `expr/`, `stmt/`, `decl/`, `type/`, `loops/`, `program/`, `specification/`. Every concrete node implements `getCode()` to regenerate Fortran source.

- **FortranParser** — Parses Flang JSON output into the AST. Key classes:
  - `FlangName` — enum mapping Flang JSON node names (e.g. `"MainProgram"`) to enum values
  - `FlangToClass` — maps `FlangName` → `Class<? extends FortranNode>`
  - `FortranJsonParser` — reads Flang JSON, creates skeleton nodes
  - `FortranAstBuilder` — wires up the node tree after parsing
  - `processors/Nodes.java` — maps node classes to processor methods via `ConsumerClassMap`
  - `processors/*Processors.java` — domain-specific processors (Expr, Stmt, Decl, Loop, etc.) that populate each node's children by reading from `FortranJsonResult`

- **FortranWeaver** — LARA framework integration. Uses WeaverGenerator to generate weaver abstracts from specs. Depends on FortranAst + FortranParser.

- **Fortran-JS** — TypeScript CLI (`@specs-feup/metafor`). FortranWeaver provides a Node-friendly interface for AST interaction. Depends on `@specs-feup/lara ~3.5.0`.

### Adding Support for a New Flang Node Type

1. Create an AST node class in `FortranAst/src/.../nodes/<category>/`, extending the appropriate base (e.g., `Expr`, `Stmt`, `FortranNode`). Implement `getCode()`.
2. Add an entry to `FlangName.java` (the enum value matching the Flang JSON name).
3. Add a mapping in `FlangToClass.java` (`NAME_TO_CLASS.put(FlangName.X, MyNode.class)`).
4. Add a processor method in the relevant `*Processors.java` file that extracts children from JSON data.
5. Register the processor in `Nodes.java` (`processors.put(MyNode.class, p::myProcessor)`).

### Test Structure

Tests live in `FortranParser/resources-test/fortran/parser/<feature>/`. Each test case has three files:
- `.json` — Flang JSON dump (used by `testJson()`)
- `.f90` — Fortran source (used by `testNative()`, Linux only)
- `.expected.f90` — expected `getCode()` output

`FortranParserTest` loads the JSON, builds the AST, calls `getCode()`, and compares against `.expected.f90` (whitespace-normalized).
