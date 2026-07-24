package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.enums.EnumHelper;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.StringProvider;

import java.util.Optional;

public enum FlangName implements StringProvider {
    PROGRAM,
    MAIN_PROGRAM,
    PROGRAM_UNIT,
    SUBROUTINE_SUBPROGRAM,
    SPECIFICATION_PART,
    INTERNAL_SUBPROGRAM_PART,
    DECLARATION_CONSTRUCT,
    IMPLICIT_PART,
    EXECUTION_PART,
    INTERNAL_SUBPROGRAM,
    BLOCK,
    EXECUTION_PART_CONSTRUCT,
    EXECUTABLE_CONSTRUCT,
    ALLOCATION,

    /// DECLs
    ENTITY_DECL,
    DUMMY_ARG,
    DATA_STMT_OBJECT,
    DATA_STMT_VALUE,
    DATA_STMT_REPEAT,
    DATA_STMT_CONSTANT,
    TYPE_PARAM_VALUE,
    DEFERRED,
    GENERIC_SPEC,

    /// STMTs
    STATEMENT,
    PROGRAM_STMT,
    END_PROGRAM_STMT,
    SUBROUTINE_STMT,
    END_SUBROUTINE_STMT,
    ACTION_STMT,
    PRINT_STMT,
    FORMAT_STMT,
    NAME,
    FORMAT,
    OUTPUT_ITEM,
    TYPE_DECLARATION_STMT,
    ASSIGNMENT_STMT,
    NON_LABEL_DO_STMT,
    END_DO_STMT,
    DO_CONSTRUCT,
    CALL_STMT,
    COMPILER_DIRECTIVE,
    GOTO_STMT,
    WRITE_STMT,
    IO_UNIT,
    IO_CONTROL_SPEC,
    CONTAINS_STMT,
    ALLOCATE_STMT,
    DEALLOCATE_STMT,
    USE_STMT,
    ONLY,
    RENAME,
    NAMES,
    OPERATORS,
    CONTINUE_STMT,
    PARAMETER_STMT,
    STOP_STMT,
    DATA_STMT,
    DATA_STMT_SET,
    COMMON_STMT,
    COMMON_STMT_BLOCK,
    COMMON_BLOCK_OBJECT,
    RETURN_STMT,
    DIMENSION_STMT,
    DECLARATION,

    /// Conditional Statements
    IF_CONSTRUCT,
    IF_THEN_STMT,
    ELSE_IF_BLOCK,
    ELSE_IF_STMT,
    ELSE_BLOCK,
    ELSE_STMT,
    END_IF_STMT,
    IF_STMT,

    CASE_CONSTRUCT,
    SELECT_CASE_STMT,
    CASE,
    CASE_STMT,
    CASE_SELECTOR,
    CASE_VALUE_RANGE,
    RANGE,
    DEFAULT,
    END_SELECT_STMT,

    // Variables
    VARIABLE,
    DESIGNATOR,
    DATA_REF,
    FUNCTION_REFERENCE,

    /// EXPRs
    EXPR,
    LITERAL_CONSTANT,
    CHAR_LITERAL_CONSTANT,
    INT_LITERAL_CONSTANT,
    SIGNED_INT_LITERAL_CONSTANT,
    LOGICAL_LITERAL_CONSTANT,
    REAL_LITERAL_CONSTANT,
    SIGN,
    SIGNED_REAL_LITERAL_CONSTANT,
    KIND_PARAM,
    STAR,
    PARENTHESES,
    UNARY_PLUS,
    NEGATE,
    NOT("NOT"),
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    EQ("EQ"),
    NE("NE"),
    LT("LT"),
    LE("LE"),
    GT("GT"),
    GE("GE"),
    AND("AND"),
    OR("OR"),
    EQV("EQV"),
    NEQV("NEQV"),
    CONCAT,
    SCALAR,
    PROCEDURE_DESIGNATOR,
    ACTUAL_ARG_SPEC,
    ACTUAL_ARG,
    CALL,
    NAMED_LITERAL,
    COMPLEX_PART,
    COMPLEX_LITERAL_CONSTANT,
    SUBSTRING,
    SUBSTRING_RANGE,

    // ARRAYs
    ARRAY_CONSTRUCTOR,
    AC_SPEC,
    AC_VALUE,
    ARRAY_ELEMENT,
    SECTION_SUBSCRIPT,
    SUBSCRIPT,
    SUBSCRIPT_TRIPLET,
    AC_IMPLIED_DO,
    AC_IMPLIED_DO_CONTROL,

    /// TYPEs
    DECLARATION_TYPE_SPEC,
    INTEGER_TYPE_SPEC,
    KIND_SELECTOR,
    STAR_SIZE,
    DOUBLE_PRECISION,
    LOGICAL,
    CHARACTER,
    REAL,
    CHAR_SELECTOR,
    CHAR_LENGTH,
    LENGTH_SELECTOR,
    LENGTH_AND_KIND,
    COMPLEX,

    /// LOOP
    LOOP_BOUNDS,
    LOOP_CONTROL,
    CONCURRENT,
    CONCURRENT_HEADER,
    CONCURRENT_CONTROL,
    LOCALITY_SPEC,

    /// OPENMP
    OPENMP_CONSTRUCT("OpenMPConstruct"),
    OMP_BLOCK_CONSTRUCT,
    OPENMP_LOOP_CONSTRUCT("OpenMPLoopConstruct"),
    OMP_BEGIN_DIRECTIVE,
    OMP_BLOCK_DIRECTIVE,
    OMP_BEGIN_LOOP_DIRECTIVE,
    OMP_END_LOOP_DIRECTIVE,
    OMP_LOOP_DIRECTIVE,
    SHARED,
    PRIVATE,
    FIRST_PRIVATE("Firstprivate"),
    OMP_OBJECT,
    OMP_OBJECT_LIST,
    OMP_CLAUSE,
    OMP_CLAUSE_LIST,
    OMP_REDUCTION_CLAUSE,
    REDUCTION,
    OMP_DIRECTIVE_NAME,
    MODIFIER,
    NOWAIT,

    // ATTRIBUTES
    DEFERRED_SHAPE_SPEC_LIST,
    SPECIFICATION_EXPR,
    ATTR_SPEC,
    ARRAY_SPEC,
    EXPLICIT_SHAPE_SPEC,
    ALLOCATE_SHAPE_SPEC,
    ALLOCATABLE,
    ASYNCHRONOUS,
    INTENT_SPEC,
    PARAMETER,


    // OTHER
    INITIALIZATION,
    NAME_VALUE,
    ALLOCATE_OBJECT,
    ALLOC_OPT,
    STAT_VARIABLE,
    NAMED_CONSTANT,
    NAMED_CONSTANT_DEF;

    private static final Lazy<EnumHelper<FlangName>> HELPER = EnumHelper.newLazyHelper(FlangName.class);

    private final String string;

    FlangName() {
        this.string = SpecsStrings.toCamelCase(name());
    }

    FlangName(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }

    /**
     * Converts string to enum using the name defined in this enum string (e.g., Program instead of PROGRAM).
     *
     * @param name
     * @return
     */
    public static Optional<FlangName> convertTry(String name) {
        return HELPER.get().fromNameTry(name);
    }

    public boolean isStmt() {
        if (name().endsWith("_STMT")) {
            return true;
        }

        return false;
    }

    public String getStmtAttr() {
        return "Statement<" + getString() + ">";
    }

    public String getUnlabeledStmtAttr() {
        return "UnlabeledStatement<" + getString() + ">";
    }
}
