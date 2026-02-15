import { registerSourceCodes } from "@specs-feup/lara/jest/jestHelpers.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import { Joinpoint, Statement } from "./Joinpoints.js";

const code = `{"nodes": [
{
"id": "0x561b659ea400-Program",
"ProgramUnit": [
"0x561b65a12df0-ProgramUnit"]
},
{
"id": "0x561b65a12df0-ProgramUnit",
"value": "0x561b65a13850-MainProgram"
},
{
"id": "0x561b65a13850-MainProgram",
"Statement<ProgramStmt>": "0x561b65a13998-Statement",
"SpecificationPart": "0x561b65a138f0-SpecificationPart",
"ExecutionPart": "0x561b65a138d8-ExecutionPart",
"Statement<EndProgramStmt>": "0x561b65a13850-Statement"
},
{
"id": "0x561b65a13998-Statement",
"statement": "0x561b65a139a8-ProgramStmt",
"label": "null",
"source": "program hello"
},
{
"id": "0x561b65a139a8-ProgramStmt",
"Name": "0x561b65a139a8-Name"
},
{
"id": "0x561b65a139a8-Name",
"source": "hello"
},
{
"id": "0x561b65a138f0-SpecificationPart",
"ImplicitPart": "0x561b65a13908-ImplicitPart"
},
{
"id": "0x561b65a13908-ImplicitPart"
},
{
"id": "0x561b65a138d8-ExecutionPart",
"ExecutionPartConstruct": [
"0x561b65a15d00-ExecutionPartConstruct",
"0x561b65a160a0-ExecutionPartConstruct"]
},
{
"id": "0x561b65a138d8-ExecutionPartConstruct"
},
{
"id": "0x561b65a15d00-ExecutionPartConstruct",
"value": "0x561b65a15d00-ExecutableConstruct"
},
{
"id": "0x561b65a15d00-ExecutableConstruct",
"value<ActionStmt>": "0x561b65a15d00-Statement"
},
{
"id": "0x561b65a15d00-Statement",
"statement": "0x561b65a15d10-ActionStmt",
"label": "null",
"source": "print *, 'Hello, World!'"
},
{
"id": "0x561b65a15d10-ActionStmt",
"value": "0x561b65a15b80-PrintStmt"
},
{
"id": "0x561b65a15b80-PrintStmt",
"Format": "0x561b65a15b98-Format",
"OutputItem": [
"0x561b65a13770-OutputItem"]
},
{
"id": "0x561b65a15b98-Format",
"value": "0x561b65a15b98-Star"
},
{
"id": "0x561b65a15b98-Star"
},
{
"id": "0x561b65a13770-OutputItem",
"value": "0x561b65a13770-Expr"
},
{
"id": "0x561b65a13770-Expr",
"value": "0x561b65a13790-LiteralConstant"
},
{
"id": "0x561b65a13790-LiteralConstant",
"value": "0x561b65a13790-CharLiteralConstant"
},
{
"id": "0x561b65a13790-CharLiteralConstant",
"string": "Hello, World!"
},
{
"id": "0x561b65a160a0-ExecutionPartConstruct",
"value": "0x561b65a160a0-ExecutableConstruct"
},
{
"id": "0x561b65a160a0-ExecutableConstruct",
"value<ActionStmt>": "0x561b65a160a0-Statement"
},
{
"id": "0x561b65a160a0-Statement",
"statement": "0x561b65a160b0-ActionStmt",
"label": "null",
"source": "print '(A, F6.3)', 'Value = ', 3"
},
{
"id": "0x561b65a160b0-ActionStmt",
"value": "0x561b65a15f90-PrintStmt"
},
{
"id": "0x561b65a15f90-PrintStmt",
"Format": "0x561b65a15fa8-Format",
"OutputItem": [
"0x561b65a15eb0-OutputItem",
"0x561b65a15dc0-OutputItem"]
},
{
"id": "0x561b65a15fa8-Format",
"value": "0x561b65a15fa8-Expr"
},
{
"id": "0x561b65a15fa8-Expr",
"value": "0x561b65a15fc8-LiteralConstant"
},
{
"id": "0x561b65a15fc8-LiteralConstant",
"value": "0x561b65a15fc8-CharLiteralConstant"
},
{
"id": "0x561b65a15fc8-CharLiteralConstant",
"string": "(A, F6.3)"
},
{
"id": "0x561b65a15eb0-OutputItem",
"value": "0x561b65a15eb0-Expr"
},
{
"id": "0x561b65a15eb0-Expr",
"value": "0x561b65a15ed0-LiteralConstant"
},
{
"id": "0x561b65a15ed0-LiteralConstant",
"value": "0x561b65a15ed0-CharLiteralConstant"
},
{
"id": "0x561b65a15ed0-CharLiteralConstant",
"string": "Value = "
},
{
"id": "0x561b65a15dc0-OutputItem",
"value": "0x561b65a15dc0-Expr"
},
{
"id": "0x561b65a15dc0-Expr",
"value": "0x561b65a15de0-LiteralConstant"
},
{
"id": "0x561b65a15de0-LiteralConstant",
"value": "0x561b65a15de0-IntLiteralConstant"
},
{
"id": "0x561b65a15de0-IntLiteralConstant",
"CharBlock": "3"
},
{
"id": "0x561b65a13850-Statement",
"statement": "0x561b65a13860-EndProgramStmt",
"label": "null",
"source": "end program hello"
},
{
"id": "0x561b65a13860-EndProgramStmt",
"Name": "0x561b65a13860-Name"
},
{
"id": "0x561b65a13860-Name",
"source": "hello"
}],
"enums": {
  "Fortran::common::CUDADataAttr": ["Constant", "Device", "Managed", "Pinned", "Shared", "Texture", "Unified"],
  "Fortran::common::CUDASubprogramAttrs": ["Host", "Device", "HostDevice", "Global", "Grid_Global"],
  "Fortran::common::OpenACCDeviceType": ["Star", "Default", "Nvidia", "Radeon", "Host", "Multicore", "None"],
  "Fortran::parser::AccDataModifier::Modifier": ["ReadOnly", "Zero"],
  "Fortran::parser::AccessSpec::Kind": ["Public", "Private"],
  "Fortran::parser::BindEntity::Kind": ["Object", "Common"],
  "Fortran::parser::ConnectSpec::CharExpr::Kind": ["Access", "Action", "Asynchronous", "Blank", "Decimal", "Delim", "Encoding", "Form", "Pad", "Position", "Round", "Sign", "Carriagecontrol", "Convert", "Dispose"],
  "Fortran::parser::DefinedOperator::IntrinsicOperator": ["Power", "Multiply", "Divide", "Add", "Subtract", "Concat", "LT", "LE", "EQ", "NE", "GE", "GT", "NOT", "AND", "OR", "EQV", "NEQV"],
  "Fortran::parser::ImplicitStmt::ImplicitNoneNameSpec": ["External", "Type"],
  "Fortran::parser::InquireSpec::CharVar::Kind": ["Access", "Action", "Asynchronous", "Blank", "Decimal", "Delim", "Direct", "Encoding", "Form", "Formatted", "Iomsg", "Name", "Pad", "Position", "Read", "Readwrite", "Round", "Sequential", "Sign", "Stream", "Status", "Unformatted", "Write", "Carriagecontrol", "Convert", "Dispose"],
  "Fortran::parser::InquireSpec::IntVar::Kind": ["Iostat", "Nextrec", "Number", "Pos", "Recl", "Size"],
  "Fortran::parser::InquireSpec::LogVar::Kind": ["Exist", "Named", "Opened", "Pending"],
  "Fortran::parser::IntentSpec::Intent": ["In", "Out", "InOut"],
  "Fortran::parser::IoControlSpec::CharExpr::Kind": ["Advance", "Blank", "Decimal", "Delim", "Pad", "Round", "Sign"],
  "Fortran::parser::ReductionOperator::Operator": ["Plus", "Multiply", "Max", "Min", "Iand", "Ior", "Ieor", "And", "Or", "Eqv", "Neqv"],
  "Fortran::parser::OmpTraitSelectorName::Value": ["Arch", "Atomic_Default_Mem_Order", "Condition", "Device_Num", "Extension", "Isa", "Kind", "Requires", "Simd", "Uid", "Vendor"],
  "Fortran::parser::OmpTraitSetSelectorName::Value": ["Construct", "Device", "Implementation", "Target_Device", "User"],
  "Fortran::parser::OmpMapType::Value": ["Alloc", "Delete", "From", "Release", "To", "Tofrom"],
  "Fortran::parser::OmpMapTypeModifier::Value": ["Always", "Close", "Present", "Ompx_Hold"],
  "Fortran::parser::OmpAtClause::ActionTime": ["Compilation", "Execution"],
  "Fortran::parser::OmpSeverityClause::Severity": ["Fatal", "Warning"],
  "Fortran::parser::OmpCancelType::Type": ["Parallel", "Sections", "Do", "Taskgroup"],
  "Fortran::parser::OmpDefaultClause::DataSharingAttribute": ["Private", "Firstprivate", "Shared", "None"],
  "Fortran::parser::OmpVariableCategory::Value": ["Aggregate", "All", "Allocatable", "Pointer", "Scalar"],
  "Fortran::parser::OmpDefaultmapClause::ImplicitBehavior": ["Alloc", "To", "From", "Tofrom", "Firstprivate", "None", "Default"],
  "Fortran::parser::OmpDependenceType::Value": ["Sink", "Source"],
  "Fortran::parser::OmpTaskDependenceType::Value": ["In", "Out", "Inout", "Inoutset", "Mutexinoutset", "Depobj"],
  "Fortran::parser::OmpExpectation::Value": ["Present"],
  "Fortran::parser::OmpLastprivateModifier::Value": ["Conditional"],
  "Fortran::parser::OmpLinearModifier::Value": ["Ref", "Uval", "Val"],
  "Fortran::parser::OmpOrderClause::Ordering": ["Concurrent"],
  "Fortran::parser::OmpOrderModifier::Value": ["Reproducible", "Unconstrained"],
  "Fortran::parser::OmpPrescriptiveness::Value": ["Strict"],
  "Fortran::parser::OmpBindClause::Binding": ["Parallel", "Teams", "Thread"],
  "Fortran::parser::OmpProcBindClause::AffinityPolicy": ["Close", "Master", "Spread", "Primary"],
  "Fortran::parser::OmpReductionModifier::Value": ["Default", "Inscan", "Task"],
  "Fortran::parser::OmpScheduleClause::Kind": ["Static", "Dynamic", "Guided", "Auto", "Runtime"],
  "Fortran::parser::OmpDeviceModifier::Value": ["Ancestor", "Device_Num"],
  "Fortran::parser::OmpDeviceTypeClause::DeviceTypeDescription": ["Any", "Host", "Nohost"],
  "Fortran::parser::OmpChunkModifier::Value": ["Simd"],
  "Fortran::parser::OmpOrderingModifier::Value": ["Monotonic", "Nonmonotonic", "Simd"],
  "Fortran::common::OmpAtomicDefaultMemOrderType": ["SeqCst", "AcqRel", "Relaxed"],
  "Fortran::parser::ProcedureStmt::Kind": ["ModuleProcedure", "Procedure"],
  "Fortran::parser::SavedEntity::Kind": ["Entity", "Common"],
  "Fortran::parser::StopStmt::Kind": ["Stop", "ErrorStop"],
  "Fortran::parser::UseStmt::ModuleNature": ["Intrinsic", "Non_Intrinsic"]
}
}
`;

describe("Query", () => {
  registerSourceCodes({ "test.json": code });

  it("should be able to search for statements", () => {
    const lst: string[] = [];
    for (const stmt of Query.search(Statement)) {
      lst.push(stmt.code);
    }
    expect(lst.length).toBe(2);
    expect(lst[0]).toEqual('PRINT *, "Hello, World!"');
    expect(lst[1]).toEqual('PRINT "(A, F6.3)", "Value = ", 3');
  });

  it("should be able to insert code", () => {
    for (const stmt of Query.search(Statement)) {
      stmt.insert("before", "! A comment");
    }

    const lines = (Query.root() as Joinpoint).code.split("\n");
    expect(lines.length).toBe(9);
    expect(lines[0].trim()).toEqual("! File 'test.json'");
    expect(lines[1].trim()).toEqual("");
    expect(lines[2].trim()).toEqual("PROGRAM hello");
    expect(lines[3].trim()).toEqual("! A comment");
    expect(lines[4].trim()).toEqual('PRINT *, "Hello, World!"');
    expect(lines[5].trim()).toEqual("! A comment");
    expect(lines[6].trim()).toEqual('PRINT "(A, F6.3)", "Value = ", 3');
    expect(lines[7].trim()).toEqual("END PROGRAM hello");
    expect(lines[8].trim()).toEqual("");
  });
});
