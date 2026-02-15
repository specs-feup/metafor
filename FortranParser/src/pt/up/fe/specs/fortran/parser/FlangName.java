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
    SPECIFICATION_PART,
    DECLARATION_CONSTRUCT,
    IMPLICIT_PART,
    EXECUTION_PART,
    BLOCK,
    EXECUTION_PART_CONSTRUCT,
    EXECUTABLE_CONSTRUCT,

    /// DECLs
    ENTITY_DECL,

    /// STMTs
    PROGRAM_STMT,
    END_PROGRAM_STMT,
    ACTION_STMT,
    PRINT_STMT,
    FORMAT_STMT,
    NAME,
    FORMAT,
    OUTPUT_ITEM,
    TYPE_DECLARATION_STMT,
    ASSIGNMENT_STMT,

    // Variables
    VARIABLE,
    DESIGNATOR,
    DATA_REF,

    /// EXPRs
    EXPR,
    LITERAL_CONSTANT,
    CHAR_LITERAL_CONSTANT,
    INT_LITERAL_CONSTANT,
    LOGICAL_LITERAL_CONSTANT,
    STAR,

    /// TYPEs
    DECLARATION_TYPE_SPEC,
    INTEGER_TYPE_SPEC,
    LOGICAL,

    // OTHER
    INITIALIZATION;

    private static final Lazy<EnumHelper<FlangName>> HELPER = EnumHelper.newLazyHelper(FlangName.class);

    private final String string;

    FlangName() {
        this.string = SpecsStrings.toCamelCase(name());
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

}
