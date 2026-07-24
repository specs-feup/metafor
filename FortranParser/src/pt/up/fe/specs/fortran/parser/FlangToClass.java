package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;
import pt.up.fe.specs.fortran.ast.nodes.alloc.StatAllocOption;
import pt.up.fe.specs.fortran.ast.nodes.decl.*;
import pt.up.fe.specs.fortran.ast.nodes.decl.typeparam.DeferredTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.decl.typeparam.ExprTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.decl.typeparam.StarTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.decl.typeparam.TypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpNowaitClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpReductionClause;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.specification.NamedConstantDef;
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
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.ConstLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.KindParamLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.LenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.ParamLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.AllocateShapeSpecification;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.DeferredShapeSpecList;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.ExplicitShapeSpecification;
import pt.up.fe.specs.fortran.ast.nodes.utils.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FlangToClass {

    private static final Map<FlangName, Class<? extends FortranNode>> NAME_TO_CLASS = new HashMap<>();
    private static final Map<FlangName, Function<FlangAttributes, Optional<Class<? extends FortranNode>>>>
            NAME_TO_CONCRETE_CLASS = new HashMap<>();

    static {
        NAME_TO_CLASS.put(FlangName.PROGRAM, FortranFile.class);
        NAME_TO_CLASS.put(FlangName.MAIN_PROGRAM, MainProgram.class);
        NAME_TO_CLASS.put(FlangName.PROGRAM_STMT, ProgramStmt.class);
        NAME_TO_CLASS.put(FlangName.END_PROGRAM_STMT, EndProgramStmt.class);
        NAME_TO_CLASS.put(FlangName.SPECIFICATION_PART, Specification.class);
        NAME_TO_CLASS.put(FlangName.EXECUTION_PART, Execution.class);
        NAME_TO_CLASS.put(FlangName.INTERNAL_SUBPROGRAM_PART, InternalSubprogram.class);
        NAME_TO_CLASS.put(FlangName.SUBROUTINE_SUBPROGRAM, Subroutine.class);
        NAME_TO_CLASS.put(FlangName.SUBROUTINE_STMT, SubroutineStmt.class);
        NAME_TO_CLASS.put(FlangName.END_SUBROUTINE_STMT, EndSubroutineStmt.class);
        NAME_TO_CLASS.put(FlangName.ALLOCATION, Allocation.class);

        /// DECLs
        NAME_TO_CLASS.put(FlangName.ENTITY_DECL, EntityDecl.class);
        NAME_TO_CLASS.put(FlangName.DUMMY_ARG, Parameter.class);
        NAME_TO_CONCRETE_CLASS.put(FlangName.DUMMY_ARG, FlangToClass::chooseParameter);
        NAME_TO_CLASS.put(FlangName.DATA_STMT_VALUE, DataStmtValue.class);
        NAME_TO_CLASS.put(FlangName.TYPE_PARAM_VALUE, TypeParamValue.class);
        NAME_TO_CONCRETE_CLASS.put(FlangName.TYPE_PARAM_VALUE, FlangToClass::chooseTypeParamValue);

        /// STMTs
        NAME_TO_CLASS.put(FlangName.PRINT_STMT, PrintStmt.class);
        NAME_TO_CLASS.put(FlangName.FORMAT_STMT, FormatStmt.class);
        NAME_TO_CLASS.put(FlangName.TYPE_DECLARATION_STMT, TypeDeclarationStmt.class);
        NAME_TO_CLASS.put(FlangName.ASSIGNMENT_STMT, AssignmentStmt.class);
        NAME_TO_CLASS.put(FlangName.DO_CONSTRUCT, DoConstruct.class);
        NAME_TO_CLASS.put(FlangName.NON_LABEL_DO_STMT, DoStmt.class);
        NAME_TO_CLASS.put(FlangName.END_DO_STMT, EndDoStmt.class);
        NAME_TO_CLASS.put(FlangName.COMPILER_DIRECTIVE, CompilerDirective.class);
        NAME_TO_CLASS.put(FlangName.GOTO_STMT, GotoStmt.class);
        NAME_TO_CLASS.put(FlangName.STOP_STMT, StopStmt.class);
        NAME_TO_CLASS.put(FlangName.COMMON_STMT, CommonStmt.class);
        NAME_TO_CLASS.put(FlangName.COMMON_STMT_BLOCK, CommonBlock.class);
        NAME_TO_CLASS.put(FlangName.COMMON_BLOCK_OBJECT, CommonBlockObject.class);

        NAME_TO_CLASS.put(FlangName.IF_CONSTRUCT, IfConstruct.class);
        NAME_TO_CLASS.put(FlangName.IF_THEN_STMT, IfThenStmt.class);
        NAME_TO_CLASS.put(FlangName.ELSE_IF_BLOCK, ElseIfBlock.class);
        NAME_TO_CLASS.put(FlangName.ELSE_IF_STMT, ElseIfStmt.class);
        NAME_TO_CLASS.put(FlangName.ELSE_BLOCK, ElseBlock.class);
        NAME_TO_CLASS.put(FlangName.ELSE_STMT, ElseStmt.class);
        NAME_TO_CLASS.put(FlangName.END_IF_STMT, EndIfStmt.class);
        NAME_TO_CLASS.put(FlangName.IF_STMT, IfStmt.class);

        NAME_TO_CLASS.put(FlangName.CASE_CONSTRUCT, CaseConstruct.class);
        NAME_TO_CLASS.put(FlangName.SELECT_CASE_STMT, SelectCaseStmt.class);
        NAME_TO_CLASS.put(FlangName.CASE, CaseBlock.class);
        NAME_TO_CLASS.put(FlangName.END_SELECT_STMT, EndSelectStmt.class);

        NAME_TO_CLASS.put(FlangName.CALL_STMT, CallStmt.class);
        NAME_TO_CLASS.put(FlangName.WRITE_STMT, WriteStmt.class);
        NAME_TO_CLASS.put(FlangName.CONTAINS_STMT, ContainsStmt.class);
        NAME_TO_CLASS.put(FlangName.ALLOCATE_STMT, AllocateStmt.class);
        NAME_TO_CLASS.put(FlangName.DEALLOCATE_STMT, DeallocateStmt.class);
        NAME_TO_CLASS.put(FlangName.USE_STMT, UseStmt.class);
        NAME_TO_CONCRETE_CLASS.put(FlangName.USE_STMT, FlangToClass::chooseUseStmt);
        NAME_TO_CLASS.put(FlangName.ONLY, Only.class);
        NAME_TO_CONCRETE_CLASS.put(FlangName.ONLY, FlangToClass::chooseOnly);
        NAME_TO_CLASS.put(FlangName.NAMES, NamesRename.class);
        NAME_TO_CLASS.put(FlangName.OPERATORS, OperatorsRename.class);
        NAME_TO_CLASS.put(FlangName.CONTINUE_STMT, ContinueStmt.class);
        NAME_TO_CLASS.put(FlangName.PARAMETER_STMT, ParameterStmt.class);
        NAME_TO_CLASS.put(FlangName.NAMED_CONSTANT_DEF, NamedConstantDef.class);
        NAME_TO_CLASS.put(FlangName.DATA_STMT, DataStmt.class);
        NAME_TO_CLASS.put(FlangName.DATA_STMT_SET, DataStmtSet.class);
        NAME_TO_CLASS.put(FlangName.RETURN_STMT, ReturnStmt.class);
        NAME_TO_CLASS.put(FlangName.DIMENSION_STMT, DimensionStmt.class);
        NAME_TO_CLASS.put(FlangName.DECLARATION, DimensionDecl.class);

        /// Variables
        //NAME_TO_CLASS.put(FlangName.DATA_REF, DataRef.class);  // TODO(Process-ing): Improve this
        NAME_TO_CLASS.put(FlangName.NAME, DataRef.class);
        NAME_TO_CLASS.put(FlangName.VARIABLE, Variable.class);
        NAME_TO_CONCRETE_CLASS.put(FlangName.VARIABLE, FlangToClass::chooseVariable);

        /// EXPRs
        NAME_TO_CLASS.put(FlangName.CHAR_LITERAL_CONSTANT, StringLiteral.class);
        NAME_TO_CLASS.put(FlangName.INT_LITERAL_CONSTANT, IntLiteral.class);
        NAME_TO_CLASS.put(FlangName.SIGNED_INT_LITERAL_CONSTANT, IntLiteral.class);
        NAME_TO_CLASS.put(FlangName.KIND_SELECTOR, KindSelector.class);
        NAME_TO_CLASS.put(FlangName.LOGICAL_LITERAL_CONSTANT, LogicalLiteral.class);
        NAME_TO_CLASS.put(FlangName.REAL_LITERAL_CONSTANT, RealLiteral.class);
        NAME_TO_CLASS.put(FlangName.SIGNED_REAL_LITERAL_CONSTANT, RealLiteral.class);
        NAME_TO_CLASS.put(FlangName.FORMAT, Format.class);
        NAME_TO_CLASS.put(FlangName.STAR, Star.class);
        NAME_TO_CLASS.put(FlangName.PARENTHESES, ParenExpr.class);
        NAME_TO_CLASS.put(FlangName.UNARY_PLUS, UnaryOperator.class);
        NAME_TO_CLASS.put(FlangName.NEGATE, UnaryOperator.class);
        NAME_TO_CLASS.put(FlangName.NOT, UnaryOperator.class);
        NAME_TO_CLASS.put(FlangName.ADD, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.SUBTRACT, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.MULTIPLY, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.DIVIDE, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.EQ, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.NE, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.LT, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.LE, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.GT, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.GE, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.AND, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.CONCAT, BinaryOperator.class);
        NAME_TO_CLASS.put(FlangName.ARRAY_CONSTRUCTOR, ArrayConstructor.class);
        NAME_TO_CLASS.put(FlangName.AC_SPEC, AcSpecification.class);
        NAME_TO_CLASS.put(FlangName.ARRAY_ELEMENT, ArraySubscriptExpr.class);
        NAME_TO_CLASS.put(FlangName.SUBSCRIPT, Subscript.class);
        NAME_TO_CLASS.put(FlangName.SUBSCRIPT_TRIPLET, SubscriptTriplet.class);
        NAME_TO_CLASS.put(FlangName.CALL, Call.class);
        NAME_TO_CLASS.put(FlangName.ACTUAL_ARG_SPEC, Argument.class);
        NAME_TO_CLASS.put(FlangName.AC_IMPLIED_DO, AcImpliedDo.class);
        NAME_TO_CLASS.put(FlangName.AC_IMPLIED_DO_CONTROL, AcImpliedDoControl.class);
        NAME_TO_CLASS.put(FlangName.NAMED_CONSTANT, NamedLiteral.class);
        NAME_TO_CLASS.put(FlangName.COMPLEX_LITERAL_CONSTANT, ComplexLiteral.class);
        NAME_TO_CLASS.put(FlangName.COMPLEX_PART, ComplexPart.class);
        NAME_TO_CONCRETE_CLASS.put(FlangName.COMPLEX_PART, FlangToClass::chooseComplexPart);
        NAME_TO_CLASS.put(FlangName.SUBSTRING, Substring.class);

        /// TYPEs
        NAME_TO_CLASS.put(FlangName.INTEGER_TYPE_SPEC, IntegerType.class);
        NAME_TO_CLASS.put(FlangName.LOGICAL, LogicalType.class);
        NAME_TO_CLASS.put(FlangName.DOUBLE_PRECISION, DoublePrecisionType.class);
        NAME_TO_CLASS.put(FlangName.CHARACTER, CharacterType.class);
        NAME_TO_CLASS.put(FlangName.REAL, RealType.class);
        NAME_TO_CLASS.put(FlangName.COMPLEX, ComplexType.class);
        NAME_TO_CLASS.put(FlangName.CHAR_LENGTH, ConstLenSelector.class);
        NAME_TO_CLASS.put(FlangName.LENGTH_SELECTOR, LenSelector.class);
        NAME_TO_CONCRETE_CLASS.put(FlangName.LENGTH_SELECTOR, FlangToClass::chooseLengthSelector);
        NAME_TO_CLASS.put(FlangName.LENGTH_AND_KIND, KindParamLenSelector.class);

        ///  LOOP
        NAME_TO_CLASS.put(FlangName.LOOP_BOUNDS, RangeLoopControl.class);
        NAME_TO_CLASS.put(FlangName.CONCURRENT, ConcurrentLoopControl.class);
        NAME_TO_CLASS.put(FlangName.CONCURRENT_CONTROL, ConcurrentRange.class);

        ///  ATTRIBUTES
        NAME_TO_CLASS.put(FlangName.ARRAY_SPEC, ArraySpecification.class);
        NAME_TO_CLASS.put(FlangName.ALLOCATABLE, AllocatableKeyword.class);
        NAME_TO_CLASS.put(FlangName.INTENT_SPEC, IntentSpec.class);
        NAME_TO_CLASS.put(FlangName.PARAMETER, ParameterKeyword.class);

        ///  SHAPES
        NAME_TO_CLASS.put(FlangName.EXPLICIT_SHAPE_SPEC, ExplicitShapeSpecification.class);
        NAME_TO_CLASS.put(FlangName.DEFERRED_SHAPE_SPEC_LIST, DeferredShapeSpecList.class);
        NAME_TO_CLASS.put(FlangName.ALLOCATE_SHAPE_SPEC, AllocateShapeSpecification.class);

        ///  UTILs
        NAME_TO_CLASS.put(FlangName.NAME_VALUE, NameValue.class);
        NAME_TO_CLASS.put(FlangName.IO_UNIT, IoUnit.class);
        NAME_TO_CLASS.put(FlangName.IO_CONTROL_SPEC, IoControlSpec.class);
        NAME_TO_CLASS.put(FlangName.STAT_VARIABLE, StatAllocOption.class);

        /// OPENMP
        NAME_TO_CLASS.put(FlangName.OMP_BLOCK_CONSTRUCT, OmpBlockConstruct.class);
        NAME_TO_CLASS.put(FlangName.OPENMP_LOOP_CONSTRUCT, OmpLoopConstruct.class);
        NAME_TO_CLASS.put(FlangName.SHARED, OmpDataSharingClause.class);
        NAME_TO_CLASS.put(FlangName.PRIVATE, OmpDataSharingClause.class);
        NAME_TO_CLASS.put(FlangName.FIRST_PRIVATE, OmpDataSharingClause.class);
        NAME_TO_CLASS.put(FlangName.REDUCTION, OmpReductionClause.class);
        NAME_TO_CLASS.put(FlangName.NOWAIT, OmpNowaitClause.class);
    }

    private static Optional<Class<? extends FortranNode>> chooseComplexPart(FlangAttributes attrs) {
        return switch (attrs.getVariantKey()) {
            case "SignedIntLiteralConstant" -> Optional.of(IntComplexPart.class);
            case "SignedRealLiteralConstant" -> Optional.of(RealComplexPart.class);
            case "NamedConstant" -> Optional.of(NamedComplexPart.class);
            default -> Optional.empty();
        };
    }

    private static Optional<Class<? extends FortranNode>> chooseLengthSelector(FlangAttributes attrs) {
        return attrs.has("TypeParamValue")
                ? Optional.of(ParamLenSelector.class)
                : Optional.empty();
    }

    private static Optional<Class<? extends FortranNode>> chooseOnly(FlangAttributes attrs) {
        return switch (attrs.getVariantKey()) {
            case "GenericSpec" -> Optional.of(OnlyGenericSpec.class);
            case "Name" -> Optional.of(UseName.class);
            default -> Optional.empty();
        };
    }

    private static Optional<Class<? extends FortranNode>> chooseParameter(FlangAttributes attrs) {
        return switch (attrs.getVariantKey()) {
            case "Name" -> Optional.of(NamedParameter.class);
            case "Star" -> Optional.of(StarParameter.class);
            default -> Optional.empty();
        };
    }

    private static Optional<Class<? extends FortranNode>> chooseTypeParamValue(FlangAttributes attrs) {
        return switch (attrs.getVariantKey()) {
            case "Expr" -> Optional.of(ExprTypeParamValue.class);
            case "Star" -> Optional.of(StarTypeParamValue.class);
            case "Deferred" -> Optional.of(DeferredTypeParamValue.class);
            default -> Optional.empty();
        };
    }

    private static Optional<Class<? extends FortranNode>> chooseUseStmt(FlangAttributes attrs) {
        if (attrs.has("renameList")) {
            return Optional.of(UseRenameStmt.class);
        }
        if (attrs.has("onlyList")) {
            return Optional.of(UseOnlyStmt.class);
        }
        return Optional.empty();
    }

    public static Optional<Class<? extends FortranNode>> chooseVariable(FlangAttributes attrs) {
        return switch (attrs.getVariantKey()) {
            case "Designator" -> Optional.of(DesignatorVariable.class);
            case "FunctionReference" -> Optional.of(FunctionRefVariable.class);
            default -> Optional.empty();
        };
    }


    public static boolean isClass(String type) {
        return FlangName.convertTry(type).map(NAME_TO_CLASS::containsKey).orElse(false);
    }

    public static boolean isConcreteClass(String type, FlangAttributes attrs) {
        return FlangName.convertTry(type).map(name -> {
            if (NAME_TO_CONCRETE_CLASS.containsKey(name)) {
                return NAME_TO_CONCRETE_CLASS.get(name).apply(attrs).isPresent();
            }
            return NAME_TO_CLASS.containsKey(name);
        }).orElse(false);
    }

    public static Optional<Class<? extends FortranNode>> getClass(String type) {
        return FlangName.convertTry(type).map(NAME_TO_CLASS::get);
    }

    public static Optional<Class<? extends FortranNode>> getConcreteClass(String type, FlangAttributes attrs) {
        return FlangName.convertTry(type).flatMap(name -> {
            if (NAME_TO_CONCRETE_CLASS.containsKey(name)) {
                return NAME_TO_CONCRETE_CLASS.get(name).apply(attrs);
            }
            return Optional.ofNullable(NAME_TO_CLASS.get(name));
        });
    }
}
