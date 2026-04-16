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

    /// DECLs
    ENTITY_DECL,
    DUMMY_ARG,
    DATA_STMT_VALUE,
    DATA_STMT_REPEAT,
    DATA_STMT_CONSTANT,

    /// STMTs
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
    DO_CONSTRUCT,
    CALL_STMT,
    COMPILER_DIRECTIVE,

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

    /// EXPRs
    EXPR,
    LITERAL_CONSTANT,
    CHAR_LITERAL_CONSTANT,
    INT_LITERAL_CONSTANT,
    SIGNED_INT_LITERAL_CONSTANT,
    LOGICAL_LITERAL_CONSTANT,
    REAL_LITERAL_CONSTANT,
    STAR,
    PARENTHESES,
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
    SCALAR,
    PROCEDURE_DESIGNATOR,
    ACTUAL_ARG_SPEC,
    ACTUAL_ARG,
    CALL,

    // ARRAYs
    ARRAY_CONSTRUCTOR,
    AC_SPEC,
    AC_VALUE,
    ARRAY_ELEMENT,
    AC_IMPLIED_DO,
    AC_IMPLIED_DO_CONTROL,

    /// TYPEs
    DECLARATION_TYPE_SPEC,
    INTEGER_TYPE_SPEC,
    DOUBLE_PRECISION,
    LOGICAL,
    CHARACTER,
    REAL,

    /// LOOP
    LOOP_BOUNDS,
    LOOP_CONTROL,
    CONCURRENT,
    CONCURRENT_HEADER,
    CONCURRENT_CONTROL,
    LOCALITY_SPEC,

    // ATTRIBUTES
    DEFERRED_SHAPE_SPEC_LIST,
    SPECIFICATION_EXPR,
    ATTR_SPEC,
    ARRAY_SPEC,
    EXPLICIT_SHAPE_SPEC,
    ALLOCATABLE,
    ASYNCHRONOUS,
    INTENT_SPEC,


    // OTHER
    INITIALIZATION,
    NAME_VALUE;

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
