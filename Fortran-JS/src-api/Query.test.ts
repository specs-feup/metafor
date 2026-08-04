import { registerSourceCodes } from "@specs-feup/lara/jest/jestHelpers.js";
import Query from "@specs-feup/lara/api/weaver/Query.js";
import { Joinpoint, Statement } from "./Joinpoints.js";

const code = `{
  "nodes": [
    {
      "id": "0x55bfe6503f00-Program",
      "ProgramUnit": [
        "0x55bfe6538d40-ProgramUnit"
      ]
    },
    {
      "id": "0x55bfe6538d40-ProgramUnit",
      "variantKey": "MainProgram",
      "MainProgram": "0x55bfe6531030-MainProgram"
    },
    {
      "id": "0x55bfe6531030-MainProgram",
      "Statement<ProgramStmt>": "0x55bfe6531178-Statement",
      "SpecificationPart": "0x55bfe65310d0-SpecificationPart",
      "ExecutionPart": "0x55bfe65310b8-ExecutionPart",
      "Statement<EndProgramStmt>": "0x55bfe6531030-Statement"
    },
    {
      "id": "0x55bfe6531178-Statement",
      "statement": "0x55bfe6531188-ProgramStmt",
      "source": "program HELLO"
    },
    {
      "id": "0x55bfe6531188-ProgramStmt",
      "Name": "0x55bfe6531188-Name"
    },
    {
      "id": "0x55bfe6531188-Name",
      "source": "HELLO"
    },
    {
      "id": "0x55bfe65310d0-SpecificationPart",
      "ImplicitPart": "0x55bfe65310e8-ImplicitPart"
    },
    {
      "id": "0x55bfe65310e8-ImplicitPart"
    },
    {
      "id": "0x55bfe65310b8-ExecutionPart",
      "ExecutionPartConstruct": [
        "0x55bfe6544580-ExecutionPartConstruct",
        "0x55bfe6544920-ExecutionPartConstruct"
      ]
    },
    {
      "id": "0x55bfe65310b8-ExecutionPartConstruct"
    },
    {
      "id": "0x55bfe6544580-ExecutionPartConstruct",
      "variantKey": "ExecutableConstruct",
      "ExecutableConstruct": "0x55bfe6544580-ExecutableConstruct"
    },
    {
      "id": "0x55bfe6544580-ExecutableConstruct",
      "variantKey": "Statement",
      "Statement<ActionStmt>": "0x55bfe6544580-Statement"
    },
    {
      "id": "0x55bfe6544580-Statement",
      "statement": "0x55bfe6544590-ActionStmt",
      "source": "print *, 'Hello, World!'"
    },
    {
      "id": "0x55bfe6544590-ActionStmt",
      "variantKey": "PrintStmt",
      "PrintStmt": "0x55bfe6544400-PrintStmt"
    },
    {
      "id": "0x55bfe6544400-PrintStmt",
      "Format": "0x55bfe6544418-Format",
      "OutputItem": [
        "0x55bfe65442b0-OutputItem"
      ]
    },
    {
      "id": "0x55bfe6544418-Format",
      "variantKey": "Star",
      "Star": "0x55bfe6544418-Star"
    },
    {
      "id": "0x55bfe6544418-Star"
    },
    {
      "id": "0x55bfe65442b0-OutputItem",
      "variantKey": "Expr",
      "Expr": "0x55bfe65442b0-Expr"
    },
    {
      "id": "0x55bfe65442b0-Expr",
      "variantKey": "LiteralConstant",
      "LiteralConstant": "0x55bfe65442d0-LiteralConstant"
    },
    {
      "id": "0x55bfe65442d0-LiteralConstant",
      "variantKey": "CharLiteralConstant",
      "CharLiteralConstant": "0x55bfe65442d0-CharLiteralConstant"
    },
    {
      "id": "0x55bfe65442d0-CharLiteralConstant",
      "string": "Hello, World!"
    },
    {
      "id": "0x55bfe6544920-ExecutionPartConstruct",
      "variantKey": "ExecutableConstruct",
      "ExecutableConstruct": "0x55bfe6544920-ExecutableConstruct"
    },
    {
      "id": "0x55bfe6544920-ExecutableConstruct",
      "variantKey": "Statement",
      "Statement<ActionStmt>": "0x55bfe6544920-Statement"
    },
    {
      "id": "0x55bfe6544920-Statement",
      "statement": "0x55bfe6544930-ActionStmt",
      "source": "print '(A, F6.3)', 'Value = ', 3"
    },
    {
      "id": "0x55bfe6544930-ActionStmt",
      "variantKey": "PrintStmt",
      "PrintStmt": "0x55bfe6544810-PrintStmt"
    },
    {
      "id": "0x55bfe6544810-PrintStmt",
      "Format": "0x55bfe6544828-Format",
      "OutputItem": [
        "0x55bfe6544730-OutputItem",
        "0x55bfe6544640-OutputItem"
      ]
    },
    {
      "id": "0x55bfe6544828-Format",
      "variantKey": "Expr",
      "Expr": "0x55bfe6544828-Expr"
    },
    {
      "id": "0x55bfe6544828-Expr",
      "variantKey": "LiteralConstant",
      "LiteralConstant": "0x55bfe6544848-LiteralConstant"
    },
    {
      "id": "0x55bfe6544848-LiteralConstant",
      "variantKey": "CharLiteralConstant",
      "CharLiteralConstant": "0x55bfe6544848-CharLiteralConstant"
    },
    {
      "id": "0x55bfe6544848-CharLiteralConstant",
      "string": "(A, F6.3)"
    },
    {
      "id": "0x55bfe6544730-OutputItem",
      "variantKey": "Expr",
      "Expr": "0x55bfe6544730-Expr"
    },
    {
      "id": "0x55bfe6544730-Expr",
      "variantKey": "LiteralConstant",
      "LiteralConstant": "0x55bfe6544750-LiteralConstant"
    },
    {
      "id": "0x55bfe6544750-LiteralConstant",
      "variantKey": "CharLiteralConstant",
      "CharLiteralConstant": "0x55bfe6544750-CharLiteralConstant"
    },
    {
      "id": "0x55bfe6544750-CharLiteralConstant",
      "string": "Value = "
    },
    {
      "id": "0x55bfe6544640-OutputItem",
      "variantKey": "Expr",
      "Expr": "0x55bfe6544640-Expr"
    },
    {
      "id": "0x55bfe6544640-Expr",
      "variantKey": "LiteralConstant",
      "LiteralConstant": "0x55bfe6544660-LiteralConstant"
    },
    {
      "id": "0x55bfe6544660-LiteralConstant",
      "variantKey": "IntLiteralConstant",
      "IntLiteralConstant": "0x55bfe6544660-IntLiteralConstant"
    },
    {
      "id": "0x55bfe6544660-IntLiteralConstant",
      "CharBlock": "3"
    },
    {
      "id": "0x55bfe6531030-Statement",
      "statement": "0x55bfe6531040-EndProgramStmt",
      "source": "end program HELLO"
    },
    {
      "id": "0x55bfe6531040-EndProgramStmt",
      "Name": "0x55bfe6531040-Name"
    },
    {
      "id": "0x55bfe6531040-Name",
      "source": "HELLO"
    }
  ],
  "enums": {
    "Fortran::common::CUDADataAttr": [
      "Constant",
      "Device",
      "Managed",
      "Pinned",
      "Shared",
      "Texture",
      "Unified"
    ],
    "Fortran::common::CUDASubprogramAttrs": [
      "Host",
      "Device",
      "HostDevice",
      "Global",
      "Grid_Global"
    ],
    "Fortran::common::ImportKind": [
      "Default",
      "Only",
      "None",
      "All"
    ],
    "Fortran::common::OmpDependenceKind": [
      "In",
      "Out",
      "Inout",
      "Inoutset",
      "Mutexinoutset",
      "Depobj"
    ],
    "Fortran::common::OmpMemoryOrderType": [
      "Acq_Rel",
      "Acquire",
      "Relaxed",
      "Release",
      "Seq_Cst"
    ],
    "Fortran::common::OpenACCDeviceType": [
      "Star",
      "Default",
      "Nvidia",
      "Radeon",
      "Host",
      "Multicore",
      "None"
    ],
    "Fortran::parser::AccDataModifier::Modifier": [
      "ReadOnly",
      "Zero"
    ],
    "Fortran::parser::AccessSpec::Kind": [
      "Public",
      "Private"
    ],
    "Fortran::parser::BindEntity::Kind": [
      "Object",
      "Common"
    ],
    "Fortran::parser::CompilerDirective::VectorLength::Kind": [
      "Auto",
      "Fixed",
      "Scalable"
    ],
    "Fortran::parser::ConnectSpec::CharExpr::Kind": [
      "Access",
      "Action",
      "Asynchronous",
      "Blank",
      "Decimal",
      "Delim",
      "Encoding",
      "Form",
      "Pad",
      "Position",
      "Round",
      "Sign",
      "Carriagecontrol",
      "Convert",
      "Dispose"
    ],
    "Fortran::parser::DefinedOperator::IntrinsicOperator": [
      "Power",
      "Multiply",
      "Divide",
      "Add",
      "Subtract",
      "Concat",
      "LT",
      "LE",
      "EQ",
      "NE",
      "GE",
      "GT",
      "NOT",
      "AND",
      "OR",
      "EQV",
      "NEQV"
    ],
    "Fortran::parser::ImplicitStmt::ImplicitNoneNameSpec": [
      "External",
      "Type"
    ],
    "Fortran::parser::InquireSpec::CharVar::Kind": [
      "Access",
      "Action",
      "Asynchronous",
      "Blank",
      "Decimal",
      "Delim",
      "Direct",
      "Encoding",
      "Form",
      "Formatted",
      "Iomsg",
      "Name",
      "Pad",
      "Position",
      "Read",
      "Readwrite",
      "Round",
      "Sequential",
      "Sign",
      "Stream",
      "Status",
      "Unformatted",
      "Write",
      "Carriagecontrol",
      "Convert",
      "Dispose"
    ],
    "Fortran::parser::InquireSpec::IntVar::Kind": [
      "Iostat",
      "Nextrec",
      "Number",
      "Pos",
      "Recl",
      "Size"
    ],
    "Fortran::parser::InquireSpec::LogVar::Kind": [
      "Exist",
      "Named",
      "Opened",
      "Pending"
    ],
    "Fortran::parser::IntentSpec::Intent": [
      "In",
      "Out",
      "InOut"
    ],
    "Fortran::parser::IoControlSpec::CharExpr::Kind": [
      "Advance",
      "Blank",
      "Decimal",
      "Delim",
      "Pad",
      "Round",
      "Sign"
    ],
    "Fortran::parser::ReductionOperator::Operator": [
      "Plus",
      "Multiply",
      "Max",
      "Min",
      "Iand",
      "Ior",
      "Ieor",
      "And",
      "Or",
      "Eqv",
      "Neqv"
    ],
    "Fortran::parser::OmpAccessGroup::Value": [
      "Cgroup"
    ],
    "Fortran::parser::OmpAdjustArgsClause::OmpAdjustOp::Value": [
      "Nothing",
      "Need_Device_Ptr"
    ],
    "Fortran::parser::OmpAlwaysModifier::Value": [
      "Always"
    ],
    "Fortran::parser::OmpAtClause::ActionTime": [
      "Compilation",
      "Execution"
    ],
    "Fortran::parser::OmpAttachModifier::Value": [
      "Always",
      "Never",
      "Auto"
    ],
    "Fortran::parser::OmpAutomapModifier::Value": [
      "Automap"
    ],
    "Fortran::parser::OmpBindClause::Binding": [
      "Parallel",
      "Teams",
      "Thread"
    ],
    "Fortran::parser::OmpChunkModifier::Value": [
      "Simd"
    ],
    "Fortran::parser::OmpCloseModifier::Value": [
      "Close"
    ],
    "Fortran::parser::OmpDefaultClause::DataSharingAttribute": [
      "Private",
      "Firstprivate",
      "Shared",
      "None"
    ],
    "Fortran::parser::OmpDefaultmapClause::ImplicitBehavior": [
      "Alloc",
      "To",
      "From",
      "Tofrom",
      "Firstprivate",
      "None",
      "Default",
      "Present"
    ],
    "Fortran::parser::OmpDeleteModifier::Value": [
      "Delete"
    ],
    "Fortran::parser::OmpDependenceType::Value": [
      "Sink",
      "Source"
    ],
    "Fortran::parser::OmpDeviceModifier::Value": [
      "Ancestor",
      "Device_Num"
    ],
    "Fortran::parser::OmpDeviceTypeClause::DeviceTypeDescription": [
      "Any",
      "Host",
      "Nohost"
    ],
    "Fortran::parser::OmpDirectiveSpecification::Flag": [
      "DeprecatedSyntax",
      "CrossesLabelDo"
    ],
    "Fortran::parser::OmpExpectation::Value": [
      "Present"
    ],
    "Fortran::parser::OmpInteropType::Value": [
      "Target",
      "Targetsync"
    ],
    "Fortran::parser::OmpLastprivateModifier::Value": [
      "Conditional"
    ],
    "Fortran::parser::OmpLinearModifier::Value": [
      "Ref",
      "Uval",
      "Val"
    ],
    "Fortran::parser::OmpMapType::Value": [
      "Alloc",
      "Delete",
      "From",
      "Release",
      "Storage",
      "To",
      "Tofrom"
    ],
    "Fortran::parser::OmpMapTypeModifier::Value": [
      "Always",
      "Close",
      "Present",
      "Ompx_Hold"
    ],
    "Fortran::parser::OmpObject::Invalid::Kind": [
      "BlankCommonBlock"
    ],
    "Fortran::parser::OmpOrderClause::Ordering": [
      "Concurrent"
    ],
    "Fortran::parser::OmpOrderingModifier::Value": [
      "Monotonic",
      "Nonmonotonic",
      "Simd"
    ],
    "Fortran::parser::OmpOrderModifier::Value": [
      "Reproducible",
      "Unconstrained"
    ],
    "Fortran::parser::OmpPrescriptiveness::Value": [
      "Strict"
    ],
    "Fortran::parser::OmpPresentModifier::Value": [
      "Present"
    ],
    "Fortran::parser::OmpProcBindClause::AffinityPolicy": [
      "Close",
      "Master",
      "Spread",
      "Primary"
    ],
    "Fortran::parser::OmpReductionModifier::Value": [
      "Default",
      "Inscan",
      "Task"
    ],
    "Fortran::parser::OmpRefModifier::Value": [
      "Ref_Ptee",
      "Ref_Ptr",
      "Ref_Ptr_Ptee"
    ],
    "Fortran::parser::OmpScheduleClause::Kind": [
      "Static",
      "Dynamic",
      "Guided",
      "Auto",
      "Runtime"
    ],
    "Fortran::parser::OmpSelfModifier::Value": [
      "Self"
    ],
    "Fortran::parser::OmpSeverityClause::SevLevel": [
      "Fatal",
      "Warning"
    ],
    "Fortran::parser::OmpThreadsetClause::ThreadsetPolicy": [
      "Omp_Pool",
      "Omp_Team"
    ],
    "Fortran::parser::OmpTraitSelectorName::Value": [
      "Arch",
      "Atomic_Default_Mem_Order",
      "Condition",
      "Device_Num",
      "Extension",
      "Isa",
      "Kind",
      "Requires",
      "Simd",
      "Uid",
      "Vendor"
    ],
    "Fortran::parser::OmpTraitSetSelectorName::Value": [
      "Construct",
      "Device",
      "Implementation",
      "Target_Device",
      "User"
    ],
    "Fortran::parser::OmpVariableCategory::Value": [
      "Aggregate",
      "All",
      "Allocatable",
      "Pointer",
      "Scalar"
    ],
    "Fortran::parser::OmpxHoldModifier::Value": [
      "Ompx_Hold"
    ],
    "Fortran::parser::ProcedureStmt::Kind": [
      "ModuleProcedure",
      "Procedure"
    ],
    "Fortran::parser::SavedEntity::Kind": [
      "Entity",
      "Common"
    ],
    "Fortran::parser::StopStmt::Kind": [
      "Stop",
      "ErrorStop"
    ],
    "Fortran::parser::UseStmt::ModuleNature": [
      "Intrinsic",
      "Non_Intrinsic"
    ]
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

    expect(lst.length).toBe(4);
    expect(lst[0]).toEqual('PROGRAM HELLO');
    expect(lst[1]).toEqual('PRINT *, "Hello, World!"');
    expect(lst[2]).toEqual('PRINT "(A, F6.3)", "Value = ", 3');
    expect(lst[3]).toEqual('END PROGRAM HELLO');
  });

  it("should be able to insert code", () => {
    for (const stmt of Query.search(Statement)) {
      stmt.insert("before", "! A comment");
    }

    const lines = (Query.root() as Joinpoint).code.split("\n");
    expect(lines.length).toBe(9);
    expect(lines[0].trim()).toEqual("! File 'test.json'");
    expect(lines[1].trim()).toEqual("");
    expect(lines[2].trim()).toEqual("PROGRAM HELLO");
    expect(lines[3].trim()).toEqual("! A comment");
    expect(lines[4].trim()).toEqual('PRINT *, "Hello, World!"');
    expect(lines[5].trim()).toEqual("! A comment");
    expect(lines[6].trim()).toEqual('PRINT "(A, F6.3)", "Value = ", 3');
    expect(lines[7].trim()).toEqual("END PROGRAM HELLO");
    expect(lines[8].trim()).toEqual("");
  });
});
