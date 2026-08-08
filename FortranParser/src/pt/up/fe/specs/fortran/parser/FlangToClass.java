package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.AllocOption;
import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;
import pt.up.fe.specs.fortran.ast.nodes.alloc.ExprAllocOption;
import pt.up.fe.specs.fortran.ast.nodes.alloc.VarAllocOption;
import pt.up.fe.specs.fortran.ast.nodes.decl.*;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.ComponentDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.attr.*;
import pt.up.fe.specs.fortran.ast.nodes.specification.coshape.DeferredCoshapeSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.coshape.ExplicitCoshapeSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.*;
import pt.up.fe.specs.fortran.ast.nodes.specification.funcspec.DeclTypeFunctionSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.funcspec.EmptyFunctionSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.funcspec.FunctionSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.type.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.typedef.DataComponentDefStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.typedef.DerivedTypeStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.typedef.EndTypeStmt;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DerivedDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.IntrinsicDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.StarDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.*;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.*;
import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.io.*;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpNowaitClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpReductionClause;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.InternalSubprogramPart;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.*;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.*;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.Module;
import pt.up.fe.specs.fortran.ast.nodes.specification.*;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.GenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.NameGenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.OpGenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.OtherGenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmtSet;
import pt.up.fe.specs.fortran.ast.nodes.stmt.dimstmt.DimensionDecl;
import pt.up.fe.specs.fortran.ast.nodes.stmt.dimstmt.DimensionStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.EndDoStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.CaseBlock;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.CaseConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.EndSelectStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.SelectCaseStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.*;
import pt.up.fe.specs.fortran.ast.nodes.type.*;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.AllocatableKeyword;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.IntentSpec;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.ParameterKeyword;
import pt.up.fe.specs.fortran.ast.nodes.utils.NameValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FlangToClass {
    private static final Map<FlangName, ClassMapper<? extends FortranNode>> NAME_TO_MAPPER = new HashMap<>();

    static {
        NAME_TO_MAPPER.put(FlangName.PROGRAM, ClassMapper.always(FortranFile.class));
        NAME_TO_MAPPER.put(FlangName.PROGRAM_UNIT, ClassMapper.caseFor(ProgramUnit.class)
                .ignore(FlangName.MAIN_PROGRAM)
                .map(FlangName.SUBROUTINE_SUBPROGRAM, SubprogramUnit.class)
                .map(FlangName.FUNCTION_SUBPROGRAM, SubprogramUnit.class)
                .ignore(FlangName.MODULE));
        NAME_TO_MAPPER.put(FlangName.MAIN_PROGRAM, ClassMapper.always(MainProgram.class));
        NAME_TO_MAPPER.put(FlangName.PROGRAM_STMT, ClassMapper.always(ProgramStmt.class));
        NAME_TO_MAPPER.put(FlangName.END_PROGRAM_STMT, ClassMapper.always(EndProgramStmt.class));
        NAME_TO_MAPPER.put(FlangName.SPECIFICATION_PART, ClassMapper.always(Specification.class));
        NAME_TO_MAPPER.put(FlangName.EXECUTION_PART, ClassMapper.always(Execution.class));
        NAME_TO_MAPPER.put(FlangName.INTERNAL_SUBPROGRAM_PART, ClassMapper.always(InternalSubprogramPart.class));
        NAME_TO_MAPPER.put(FlangName.SUBROUTINE_SUBPROGRAM, ClassMapper.always(Subroutine.class));
        NAME_TO_MAPPER.put(FlangName.FUNCTION_SUBPROGRAM, ClassMapper.always(Function.class));
        NAME_TO_MAPPER.put(FlangName.SUBROUTINE_STMT, ClassMapper.always(SubroutineStmt.class));
        NAME_TO_MAPPER.put(FlangName.END_SUBROUTINE_STMT, ClassMapper.always(EndSubroutineStmt.class));
        NAME_TO_MAPPER.put(FlangName.MODULE, ClassMapper.always(Module.class));
        NAME_TO_MAPPER.put(FlangName.MODULE_SUBPROGRAM_PART, ClassMapper.always(ModuleSubprogramPart.class));
        NAME_TO_MAPPER.put(FlangName.MODULE_STMT, ClassMapper.always(ModuleStmt.class));
        NAME_TO_MAPPER.put(FlangName.END_MODULE_STMT, ClassMapper.always(EndModuleStmt.class));
        NAME_TO_MAPPER.put(FlangName.ALLOCATION, ClassMapper.always(Allocation.class));

        /// DECLs
        NAME_TO_MAPPER.put(FlangName.ENTITY_DECL, ClassMapper.always(EntityDecl.class));
        NAME_TO_MAPPER.put(FlangName.DUMMY_ARG, ClassMapper.caseFor(Parameter.class)
                .map(FlangName.NAME, NamedParameter.class)
                .map(FlangName.STAR, StarParameter.class));
        NAME_TO_MAPPER.put(FlangName.DATA_STMT_VALUE, ClassMapper.always(DataStmtValue.class));
        NAME_TO_MAPPER.put(FlangName.DEFINED_OP_NAME, ClassMapper.always(NamedOperator.class));
        NAME_TO_MAPPER.put(FlangName.INTRINSIC_OPERATOR, ClassMapper.always(IntrinsicOperator.class));
        NAME_TO_MAPPER.put(FlangName.GENERIC_SPEC, ClassMapper.caseFor(GenericSpec.class)
                .map(FlangName.NAME, NameGenericSpec.class)
                .map(FlangName.DEFINED_OPERATOR, OpGenericSpec.class)
                .map(FlangName.ASSIGNMENT, OtherGenericSpec.class)
                .map(FlangName.READ_FORMATTED, OtherGenericSpec.class)
                .map(FlangName.READ_UNFORMATTED, OtherGenericSpec.class)
                .map(FlangName.WRITE_FORMATTED, OtherGenericSpec.class)
                .map(FlangName.WRITE_UNFORMATTED, OtherGenericSpec.class));
        NAME_TO_MAPPER.put(FlangName.PREFIX_SPEC, ClassMapper.caseFor(FunctionSpec.class)
                .map(FlangName.DECLARATION_TYPE_SPEC, DeclTypeFunctionSpec.class)
                .map(FlangName.ELEMENTAL, EmptyFunctionSpec.class)
                .map(FlangName.IMPURE, EmptyFunctionSpec.class)
                .map(FlangName.MODULE, EmptyFunctionSpec.class)
                .map(FlangName.NON_RECURSIVE, EmptyFunctionSpec.class)
                .map(FlangName.PURE, EmptyFunctionSpec.class)
                .map(FlangName.RECURSIVE, EmptyFunctionSpec.class));
        NAME_TO_MAPPER.put(FlangName.DERIVED_TYPE_DEF, ClassMapper.always(DerivedTypeDef.class));
        NAME_TO_MAPPER.put(FlangName.TYPE_ATTR_SPEC, ClassMapper.caseFor(TypeAttr.class)
                .map(FlangName.ABSTRACT, AbstractTypeAttr.class)
                .map(FlangName.ACCESS_SPEC, AccessTypeAttr.class)
                .map(FlangName.BIND_C, BindTypeAttr.class)
                .map(FlangName.EXTENDS, ExtendsTypeAttr.class));

        /// STMTs
        NAME_TO_MAPPER.put(FlangName.PRINT_STMT, ClassMapper.always(PrintStmt.class));
        NAME_TO_MAPPER.put(FlangName.FORMAT_STMT, ClassMapper.always(FormatStmt.class));
        NAME_TO_MAPPER.put(FlangName.TYPE_DECLARATION_STMT, ClassMapper.always(TypeDeclarationStmt.class));
        NAME_TO_MAPPER.put(FlangName.ASSIGNMENT_STMT, ClassMapper.always(AssignmentStmt.class));
        NAME_TO_MAPPER.put(FlangName.DO_CONSTRUCT, ClassMapper.always(DoConstruct.class));
        NAME_TO_MAPPER.put(FlangName.NON_LABEL_DO_STMT, ClassMapper.always(DoStmt.class));
        NAME_TO_MAPPER.put(FlangName.END_DO_STMT, ClassMapper.always(EndDoStmt.class));
        NAME_TO_MAPPER.put(FlangName.COMPILER_DIRECTIVE, ClassMapper.always(CompilerDirective.class));
        NAME_TO_MAPPER.put(FlangName.GOTO_STMT, ClassMapper.always(GotoStmt.class));
        NAME_TO_MAPPER.put(FlangName.STOP_STMT, ClassMapper.always(StopStmt.class));
        NAME_TO_MAPPER.put(FlangName.COMMON_STMT, ClassMapper.always(CommonStmt.class));
        NAME_TO_MAPPER.put(FlangName.COMMON_STMT_BLOCK, ClassMapper.always(CommonBlock.class));
        NAME_TO_MAPPER.put(FlangName.COMMON_BLOCK_OBJECT, ClassMapper.always(CommonBlockObject.class));

        NAME_TO_MAPPER.put(FlangName.IF_CONSTRUCT, ClassMapper.always(IfConstruct.class));
        NAME_TO_MAPPER.put(FlangName.IF_THEN_STMT, ClassMapper.always(IfThenStmt.class));
        NAME_TO_MAPPER.put(FlangName.ELSE_IF_BLOCK, ClassMapper.always(ElseIfBlock.class));
        NAME_TO_MAPPER.put(FlangName.ELSE_IF_STMT, ClassMapper.always(ElseIfStmt.class));
        NAME_TO_MAPPER.put(FlangName.ELSE_BLOCK, ClassMapper.always(ElseBlock.class));
        NAME_TO_MAPPER.put(FlangName.ELSE_STMT, ClassMapper.always(ElseStmt.class));
        NAME_TO_MAPPER.put(FlangName.END_IF_STMT, ClassMapper.always(EndIfStmt.class));
        NAME_TO_MAPPER.put(FlangName.IF_STMT, ClassMapper.always(IfStmt.class));

        NAME_TO_MAPPER.put(FlangName.CASE_CONSTRUCT, ClassMapper.always(CaseConstruct.class));
        NAME_TO_MAPPER.put(FlangName.SELECT_CASE_STMT, ClassMapper.always(SelectCaseStmt.class));
        NAME_TO_MAPPER.put(FlangName.CASE, ClassMapper.always(CaseBlock.class));
        NAME_TO_MAPPER.put(FlangName.END_SELECT_STMT, ClassMapper.always(EndSelectStmt.class));

        NAME_TO_MAPPER.put(FlangName.CALL_STMT, ClassMapper.always(CallStmt.class));
        NAME_TO_MAPPER.put(FlangName.CONTAINS_STMT, ClassMapper.always(ContainsStmt.class));
        NAME_TO_MAPPER.put(FlangName.ALLOCATE_STMT, ClassMapper.always(AllocateStmt.class));
        NAME_TO_MAPPER.put(FlangName.DEALLOCATE_STMT, ClassMapper.always(DeallocateStmt.class));
        NAME_TO_MAPPER.put(FlangName.USE_STMT, ClassMapper.caseFor(UseStmt.class)
                .map(FlangName.RENAME, UseRenameStmt.class)
                .map(FlangName.ONLY, UseOnlyStmt.class));
        NAME_TO_MAPPER.put(FlangName.ONLY, ClassMapper.caseFor(Only.class)
                .map(FlangName.GENERIC_SPEC, OnlyGenericSpec.class)
                .map(FlangName.NAME, UseName.class)
                .ignore(FlangName.RENAME));
        NAME_TO_MAPPER.put(FlangName.NAMES, ClassMapper.always(NamesRename.class));
        NAME_TO_MAPPER.put(FlangName.OPERATORS, ClassMapper.always(OperatorsRename.class));
        NAME_TO_MAPPER.put(FlangName.CONTINUE_STMT, ClassMapper.always(ContinueStmt.class));
        NAME_TO_MAPPER.put(FlangName.PARAMETER_STMT, ClassMapper.always(ParameterStmt.class));
        NAME_TO_MAPPER.put(FlangName.NAMED_CONSTANT_DEF, ClassMapper.always(NamedConstantDef.class));
        NAME_TO_MAPPER.put(FlangName.DATA_STMT, ClassMapper.always(DataStmt.class));
        NAME_TO_MAPPER.put(FlangName.DATA_STMT_SET, ClassMapper.always(DataStmtSet.class));
        NAME_TO_MAPPER.put(FlangName.RETURN_STMT, ClassMapper.always(ReturnStmt.class));
        NAME_TO_MAPPER.put(FlangName.DIMENSION_STMT, ClassMapper.always(DimensionStmt.class));
        NAME_TO_MAPPER.put(FlangName.DECLARATION, ClassMapper.always(DimensionDecl.class));
        NAME_TO_MAPPER.put(FlangName.NAMELIST_STMT, ClassMapper.always(NamelistStmt.class));
        NAME_TO_MAPPER.put(FlangName.GROUP, ClassMapper.always(NamelistGroup.class));
        NAME_TO_MAPPER.put(FlangName.FUNCTION_STMT, ClassMapper.always(FunctionStmt.class));
        NAME_TO_MAPPER.put(FlangName.LANGUAGE_BINDING_SPEC, ClassMapper.always(LanguageBindingSpec.class));
        NAME_TO_MAPPER.put(FlangName.END_FUNCTION_STMT, ClassMapper.always(EndFunctionStmt.class));
        NAME_TO_MAPPER.put(FlangName.EXTERNAL_STMT, ClassMapper.always(ExternalStmt.class));
        NAME_TO_MAPPER.put(FlangName.ACCESS_STMT, ClassMapper.always(AccessStmt.class));
        NAME_TO_MAPPER.put(FlangName.IMPORT_STMT, ClassMapper.always(ImportStmt.class));
        NAME_TO_MAPPER.put(FlangName.IMPLICIT_STMT, ClassMapper.caseFor(ImplicitStmt.class)
                .map(FlangName.IMPLICIT_SPEC, DefaultImplicitStmt.class)
                .map(FlangName.IMPLICIT_NONE_NAME_SPEC, ImplicitNoneStmt.class));
        NAME_TO_MAPPER.put(FlangName.IMPLICIT_SPEC, ClassMapper.always(ImplicitSpec.class));
        NAME_TO_MAPPER.put(FlangName.LETTER_SPEC, ClassMapper.always(LetterSpec.class));
        NAME_TO_MAPPER.put(FlangName.DERIVED_TYPE_STMT, ClassMapper.always(DerivedTypeStmt.class));
        NAME_TO_MAPPER.put(FlangName.DATA_COMPONENT_DEF_STMT, ClassMapper.always(DataComponentDefStmt.class));
        NAME_TO_MAPPER.put(FlangName.COMPONENT_ATTR_SPEC, ClassMapper.caseFor(ComponentAttr.class)
                .map(FlangName.ACCESS_SPEC, AccessComponentAttr.class)
                .map(FlangName.ALLOCATABLE, OtherComponentAttr.class)
                .map(FlangName.COARRAY_SPEC, CodimComponentAttr.class)
                .map(FlangName.CONTIGUOUS, OtherComponentAttr.class)
                .map(FlangName.COMPONENT_ARRAY_SPEC, DimComponentAttr.class)
                .map(FlangName.POINTER, OtherComponentAttr.class));
        NAME_TO_MAPPER.put(FlangName.COMPONENT_DECL, ClassMapper.always(ComponentDecl.class));
        NAME_TO_MAPPER.put(FlangName.END_TYPE_STMT, ClassMapper.always(EndTypeStmt.class));

        /// Variables
        //NAME_TO_CLASS.put(FlangName.DATA_REF, DataRef.class);  // TODO(Process-ing): Improve this
        NAME_TO_MAPPER.put(FlangName.NAME, ClassMapper.always(DataRef.class));
        NAME_TO_MAPPER.put(FlangName.VARIABLE, ClassMapper.caseFor(Variable.class)
                .map(FlangName.DESIGNATOR, DesignatorVariable.class)
                .map(FlangName.FUNCTION_REFERENCE, FunctionRefVariable.class));

        /// EXPRs
        NAME_TO_MAPPER.put(FlangName.CHAR_LITERAL_CONSTANT, ClassMapper.always(StringLiteral.class));
        NAME_TO_MAPPER.put(FlangName.INT_LITERAL_CONSTANT, ClassMapper.always(IntLiteral.class));
        NAME_TO_MAPPER.put(FlangName.SIGNED_INT_LITERAL_CONSTANT, ClassMapper.always(IntLiteral.class));
        NAME_TO_MAPPER.put(FlangName.KIND_SELECTOR, ClassMapper.always(KindSelector.class));
        NAME_TO_MAPPER.put(FlangName.LOGICAL_LITERAL_CONSTANT, ClassMapper.always(LogicalLiteral.class));
        NAME_TO_MAPPER.put(FlangName.REAL_LITERAL_CONSTANT, ClassMapper.always(RealLiteral.class));
        NAME_TO_MAPPER.put(FlangName.SIGNED_REAL_LITERAL_CONSTANT, ClassMapper.always(RealLiteral.class));
        NAME_TO_MAPPER.put(FlangName.PARENTHESES, ClassMapper.always(ParenExpr.class));
        NAME_TO_MAPPER.put(FlangName.UNARY_PLUS, ClassMapper.always(UnaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.NEGATE, ClassMapper.always(UnaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.NOT, ClassMapper.always(UnaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.ADD, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.SUBTRACT, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.MULTIPLY, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.DIVIDE, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.POWER, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.EQ, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.NE, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.LT, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.LE, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.GT, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.GE, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.AND, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.OR, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.EQV, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.NEQV, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.CONCAT, ClassMapper.always(BinaryOperator.class));
        NAME_TO_MAPPER.put(FlangName.ARRAY_CONSTRUCTOR, ClassMapper.always(ArrayConstructor.class));
        NAME_TO_MAPPER.put(FlangName.AC_SPEC, ClassMapper.always(AcSpecification.class));
        NAME_TO_MAPPER.put(FlangName.ARRAY_ELEMENT, ClassMapper.always(ArraySubscriptExpr.class));
        NAME_TO_MAPPER.put(FlangName.SUBSCRIPT, ClassMapper.always(Subscript.class));
        NAME_TO_MAPPER.put(FlangName.SUBSCRIPT_TRIPLET, ClassMapper.always(SubscriptTriplet.class));
        NAME_TO_MAPPER.put(FlangName.CALL, ClassMapper.always(Call.class));
        NAME_TO_MAPPER.put(FlangName.ACTUAL_ARG_SPEC, ClassMapper.always(Argument.class));
        NAME_TO_MAPPER.put(FlangName.AC_IMPLIED_DO, ClassMapper.always(AcImpliedDo.class));
        NAME_TO_MAPPER.put(FlangName.AC_IMPLIED_DO_CONTROL, ClassMapper.always(AcImpliedDoControl.class));
        NAME_TO_MAPPER.put(FlangName.NAMED_CONSTANT, ClassMapper.always(NamedLiteral.class));
        NAME_TO_MAPPER.put(FlangName.COMPLEX_LITERAL_CONSTANT, ClassMapper.always(ComplexLiteral.class));
        NAME_TO_MAPPER.put(FlangName.COMPLEX_PART, ClassMapper.caseFor(ComplexPart.class)
                .map(FlangName.SIGNED_INT_LITERAL_CONSTANT, IntComplexPart.class)
                .map(FlangName.SIGNED_REAL_LITERAL_CONSTANT, RealComplexPart.class)
                .map(FlangName.NAMED_CONSTANT, NamedComplexPart.class));
        NAME_TO_MAPPER.put(FlangName.SUBSTRING, ClassMapper.always(Substring.class));

        /// TYPEs
        NAME_TO_MAPPER.put(FlangName.INTEGER_TYPE_SPEC, ClassMapper.always(IntegerType.class));
        NAME_TO_MAPPER.put(FlangName.LOGICAL, ClassMapper.always(LogicalType.class));
        NAME_TO_MAPPER.put(FlangName.DOUBLE_PRECISION, ClassMapper.always(DoublePrecisionType.class));
        NAME_TO_MAPPER.put(FlangName.CHARACTER, ClassMapper.always(CharacterType.class));
        NAME_TO_MAPPER.put(FlangName.REAL, ClassMapper.always(RealType.class));
        NAME_TO_MAPPER.put(FlangName.COMPLEX, ClassMapper.always(ComplexType.class));
        NAME_TO_MAPPER.put(FlangName.CHAR_LENGTH, ClassMapper.caseFor(CharLenSelector.class)
                .map(FlangName.TYPE_PARAM_VALUE, ParamCharLenSelector.class)
                .map("uint64_t", ConstLenSelector.class));
        NAME_TO_MAPPER.put(FlangName.LENGTH_SELECTOR, ClassMapper.caseFor(LenSelector.class)
                .map(FlangName.TYPE_PARAM_VALUE, ParamLenSelector.class)
                .ignore(FlangName.CHAR_LENGTH));
        NAME_TO_MAPPER.put(FlangName.LENGTH_AND_KIND, ClassMapper.always(KindParamLenSelector.class));
        NAME_TO_MAPPER.put(FlangName.TYPE_PARAM_VALUE, ClassMapper.caseFor(TypeParamValue.class)
                .map(FlangName.EXPR, ExprTypeParamValue.class)
                .map(FlangName.STAR, StarTypeParamValue.class)
                .map(FlangName.DEFERRED, DeferredTypeParamValue.class));
        NAME_TO_MAPPER.put(FlangName.TYPE_PARAM_SPEC, ClassMapper.always(TypeParam.class));
        NAME_TO_MAPPER.put(FlangName.DERIVED_TYPE_SPEC, ClassMapper.always(DerivedType.class));
        NAME_TO_MAPPER.put(FlangName.DECLARATION_TYPE_SPEC, ClassMapper.caseFor(DeclType.class)
                .map(FlangName.INTRINSIC_TYPE_SPEC, IntrinsicDeclType.class)
                .map(FlangName.TYPE, DerivedDeclType.class)
                .map(FlangName.CLASS, DerivedDeclType.class)
                .map(FlangName.CLASS_STAR, StarDeclType.class)
                .map(FlangName.TYPE_STAR, StarDeclType.class));

        ///  LOOP
        NAME_TO_MAPPER.put(FlangName.LOOP_BOUNDS, ClassMapper.always(RangeLoopControl.class));
        NAME_TO_MAPPER.put(FlangName.CONCURRENT, ClassMapper.always(ConcurrentLoopControl.class));
        NAME_TO_MAPPER.put(FlangName.CONCURRENT_CONTROL, ClassMapper.always(ConcurrentRange.class));

        ///  ATTRIBUTES
        NAME_TO_MAPPER.put(FlangName.ALLOCATABLE, ClassMapper.always(AllocatableKeyword.class));
        NAME_TO_MAPPER.put(FlangName.INTENT_SPEC, ClassMapper.always(IntentSpec.class));
        NAME_TO_MAPPER.put(FlangName.PARAMETER, ClassMapper.always(ParameterKeyword.class));

        ///  SHAPES
        NAME_TO_MAPPER.put(FlangName.EXPLICIT_SHAPE_SPEC, ClassMapper.always(ExplicitShape.class));
        NAME_TO_MAPPER.put(FlangName.ALLOCATE_SHAPE_SPEC, ClassMapper.always(ExplicitShape.class));
        NAME_TO_MAPPER.put(FlangName.ALLOCATE_COSHAPE_SPEC, ClassMapper.always(ExplicitShape.class));
        NAME_TO_MAPPER.put(FlangName.ASSUMED_SHAPE_SPEC, ClassMapper.always(AssumedShape.class));
        NAME_TO_MAPPER.put(FlangName.ASSUMED_IMPLIED_SPEC, ClassMapper.always(AssumedImpliedShape.class));
        NAME_TO_MAPPER.put(FlangName.ARRAY_SPEC, ClassMapper.caseFor(ArraySpec.class)
                .map(FlangName.EXPLICIT_SHAPE_SPEC, ExplicitShapeArraySpec.class)
                .map(FlangName.ASSUMED_SHAPE_SPEC, AssumedShapeArraySpec.class)
                .ignore(FlangName.DEFERRED_SHAPE_SPEC_LIST)
                .ignore(FlangName.ASSUMED_SIZE_SPEC)
                .ignore(FlangName.IMPLIED_SHAPE_SPEC)
                .ignore(FlangName.ASSUMED_RANK_SPEC));
        NAME_TO_MAPPER.put(FlangName.DEFERRED_SHAPE_SPEC_LIST, ClassMapper.always(DeferredShapeSpec.class));
        NAME_TO_MAPPER.put(FlangName.ASSUMED_SIZE_SPEC, ClassMapper.always(AssumedSizeSpec.class));
        NAME_TO_MAPPER.put(FlangName.IMPLIED_SHAPE_SPEC, ClassMapper.always(ImpliedShapeSpec.class));
        NAME_TO_MAPPER.put(FlangName.ASSUMED_RANK_SPEC, ClassMapper.always(AssumedSizeSpec.class));
        NAME_TO_MAPPER.put(FlangName.DEFERRED_COSHAPE_SPEC_LIST, ClassMapper.always(DeferredCoshapeSpec.class));
        NAME_TO_MAPPER.put(FlangName.EXPLICIT_COSHAPE_SPEC, ClassMapper.always(ExplicitCoshapeSpec.class));

        /// IO
        NAME_TO_MAPPER.put(FlangName.OPEN_STMT, ClassMapper.always(OpenStmt.class));
        NAME_TO_MAPPER.put(FlangName.CONNECT_SPEC, ClassMapper.caseFor(ConnectSpec.class)
                .map(FlangName.FILE_UNIT_NUMBER, ExprConnectSpec.class)
                .map(FlangName.EXPR, ExprConnectSpec.class)
                .map(FlangName.CHAR_EXPR, ExprConnectSpec.class)
                .map(FlangName.MSG_VARIABLE, VarConnectSpec.class)
                .map(FlangName.STAT_VARIABLE, VarConnectSpec.class)
                .map(FlangName.RECL, ExprConnectSpec.class)
                .map(FlangName.NEWUNIT, VarConnectSpec.class)
                .map(FlangName.ERR_LABEL, ErrConnectSpec.class)
                .map(FlangName.STATUS_EXPR, ExprConnectSpec.class));
        NAME_TO_MAPPER.put(FlangName.IO_UNIT, ClassMapper.caseFor(IoControlSpec.class)
                .map(FlangName.EXPR, ExprIoControlSpec.class)
                .map(FlangName.VARIABLE, VarIoControlSpec.class)
                .map(FlangName.STAR, StarUnitIoControlSpec.class));
        NAME_TO_MAPPER.put(FlangName.REWIND_STMT, ClassMapper.always(RewindStmt.class));
        NAME_TO_MAPPER.put(FlangName.POSITION_OR_FLUSH_SPEC, ClassMapper.caseFor(PosFlushSpec.class)
                .map(FlangName.FILE_UNIT_NUMBER, UnitPosFlushSpec.class)
                .map(FlangName.MSG_VARIABLE, VarPosFlushSpec.class)
                .map(FlangName.STAT_VARIABLE, VarPosFlushSpec.class)
                .map(FlangName.ERR_LABEL, ErrPosFlushSpec.class));
        NAME_TO_MAPPER.put(FlangName.IO_CONTROL_SPEC, ClassMapper.caseFor(IoControlSpec.class)
                .ignore(FlangName.IO_UNIT)
                .map(FlangName.FORMAT, FormatIoControlSpec.class)
                .map(FlangName.NAME, NamelistIoControlSpec.class)
                .map(FlangName.CHAR_EXPR, ExprIoControlSpec.class)
                .map(FlangName.ASYNCHRONOUS, ExprIoControlSpec.class)
                .map(FlangName.END_LABEL, LabelIoControlSpec.class)
                .map(FlangName.EOR_LABEL, LabelIoControlSpec.class)
                .map(FlangName.ERR_LABEL, LabelIoControlSpec.class)
                .map(FlangName.ID_VARIABLE, VarIoControlSpec.class)
                .map(FlangName.MSG_VARIABLE, VarIoControlSpec.class)
                .map(FlangName.STAT_VARIABLE, VarIoControlSpec.class)
                .map(FlangName.POS, ExprIoControlSpec.class)
                .map(FlangName.REC, ExprIoControlSpec.class)
                .map(FlangName.SIZE, VarIoControlSpec.class));
        NAME_TO_MAPPER.put(FlangName.READ_STMT, ClassMapper.always(ReadStmt.class));
        NAME_TO_MAPPER.put(FlangName.INPUT_ITEM, ClassMapper.caseFor(InputItem.class)
                .map(FlangName.VARIABLE, VarInputItem.class)
                .map(FlangName.INPUT_IMPLIED_DO, InputImpliedDoItem.class));
        NAME_TO_MAPPER.put(FlangName.WRITE_STMT, ClassMapper.always(WriteStmt.class));
        NAME_TO_MAPPER.put(FlangName.OUTPUT_ITEM, ClassMapper.caseFor(OutputItem.class)
                .map(FlangName.EXPR, ExprOutputItem.class)
                .map(FlangName.OUTPUT_IMPLIED_DO, OutputImpliedDoItem.class));
        NAME_TO_MAPPER.put(FlangName.WAIT_STMT, ClassMapper.always(WaitStmt.class));
        NAME_TO_MAPPER.put(FlangName.WAIT_SPEC, ClassMapper.caseFor(WaitSpec.class)
                .map(FlangName.FILE_UNIT_NUMBER, ExprWaitSpec.class)
                .map(FlangName.END_LABEL, LabelWaitSpec.class)
                .map(FlangName.EOR_LABEL, LabelWaitSpec.class)
                .map(FlangName.ERR_LABEL, LabelWaitSpec.class)
                .map(FlangName.ID_EXPR, ExprWaitSpec.class)
                .map(FlangName.MSG_VARIABLE, VarWaitSpec.class)
                .map(FlangName.STAT_VARIABLE, VarWaitSpec.class));
        NAME_TO_MAPPER.put(FlangName.CLOSE_STMT, ClassMapper.always(CloseStmt.class));
        NAME_TO_MAPPER.put(FlangName.CLOSE_SPEC, ClassMapper.caseFor(CloseSpec.class)
                .map(FlangName.FILE_UNIT_NUMBER, ExprCloseSpec.class)
                .map(FlangName.STAT_VARIABLE, VarCloseSpec.class)
                .map(FlangName.MSG_VARIABLE, VarCloseSpec.class)
                .map(FlangName.ERR_LABEL, ErrCloseSpec.class)
                .map(FlangName.STATUS_EXPR, ExprCloseSpec.class));
        NAME_TO_MAPPER.put(FlangName.FORMAT, ClassMapper.caseFor(Format.class)
                .map(FlangName.EXPR, ExprFormat.class)
                .map("uint64_t", LabelFormat.class)
                .map(FlangName.STAR, StarFormat.class));

        ///  UTILs
        NAME_TO_MAPPER.put(FlangName.NAME_VALUE, ClassMapper.always(NameValue.class));
        NAME_TO_MAPPER.put(FlangName.ALLOC_OPT, ClassMapper.caseFor(AllocOption.class)
                .map(FlangName.MOLD, ExprAllocOption.class)
                .map(FlangName.SOURCE, ExprAllocOption.class)
                .map(FlangName.STAT_OR_ERRMSG, VarAllocOption.class)
                .map(FlangName.STREAM, ExprAllocOption.class)
                .map(FlangName.PINNED, VarAllocOption.class));

        /// OPENMP
        NAME_TO_MAPPER.put(FlangName.OMP_BLOCK_CONSTRUCT, ClassMapper.always(OmpBlockConstruct.class));
        NAME_TO_MAPPER.put(FlangName.OPENMP_LOOP_CONSTRUCT, ClassMapper.always(OmpLoopConstruct.class));
        NAME_TO_MAPPER.put(FlangName.SHARED, ClassMapper.always(OmpDataSharingClause.class));
        NAME_TO_MAPPER.put(FlangName.PRIVATE, ClassMapper.always(OmpDataSharingClause.class));
        NAME_TO_MAPPER.put(FlangName.FIRST_PRIVATE, ClassMapper.always(OmpDataSharingClause.class));
        NAME_TO_MAPPER.put(FlangName.REDUCTION, ClassMapper.always(OmpReductionClause.class));
        NAME_TO_MAPPER.put(FlangName.NOWAIT, ClassMapper.always(OmpNowaitClause.class));
    }

    public static boolean isClass(String type, FlangAttributes attrs) {
        return FlangName.convertTry(type)
                .flatMap(name -> Optional.ofNullable(NAME_TO_MAPPER.get(name)))
                .map(mapper -> mapper.has(attrs))
                .orElse(false);
    }

    public static Optional<Class<? extends FortranNode>> getClass(String type, FlangAttributes attrs) {
        return FlangName.convertTry(type)
                .flatMap(name -> Optional.ofNullable(NAME_TO_MAPPER.get(name)))
                .flatMap(mapper -> mapper.get(attrs));
    }
}
