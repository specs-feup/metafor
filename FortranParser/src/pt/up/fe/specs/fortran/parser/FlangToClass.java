package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.LogicalLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.StringLiteral;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.MainProgram;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.type.IntegerType;
import pt.up.fe.specs.fortran.ast.nodes.type.LogicalType;
import pt.up.fe.specs.fortran.ast.nodes.utils.Format;
import pt.up.fe.specs.fortran.ast.nodes.utils.Star;
import pt.up.fe.specs.fortran.ast.nodes.variable.DataRef;
import pt.up.fe.specs.fortran.ast.nodes.variable.Designator;
import pt.up.fe.specs.fortran.ast.nodes.variable.Variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FlangToClass {

    private static final Map<FlangName, Class<? extends FortranNode>> NAME_TO_CLASS = new HashMap<>();

    static {
        NAME_TO_CLASS.put(FlangName.PROGRAM, FortranFile.class);
        NAME_TO_CLASS.put(FlangName.MAIN_PROGRAM, MainProgram.class);
        NAME_TO_CLASS.put(FlangName.SPECIFICATION_PART, Specification.class);
        NAME_TO_CLASS.put(FlangName.EXECUTION_PART, Execution.class);

        /// DECLs
        NAME_TO_CLASS.put(FlangName.ENTITY_DECL, EntityDecl.class);

        /// STMTs
        NAME_TO_CLASS.put(FlangName.PRINT_STMT, PrintStmt.class);
        NAME_TO_CLASS.put(FlangName.FORMAT_STMT, FormatStmt.class);
        NAME_TO_CLASS.put(FlangName.TYPE_DECLARATION_STMT, TypeDeclarationStmt.class);
        NAME_TO_CLASS.put(FlangName.ASSIGNMENT_STMT, AssignmentStmt.class);

        /// Variables
        NAME_TO_CLASS.put(FlangName.DATA_REF, DataRef.class);

        /// EXPRs
        NAME_TO_CLASS.put(FlangName.CHAR_LITERAL_CONSTANT, StringLiteral.class);
        NAME_TO_CLASS.put(FlangName.INT_LITERAL_CONSTANT, IntLiteral.class);
        NAME_TO_CLASS.put(FlangName.LOGICAL_LITERAL_CONSTANT, LogicalLiteral.class);
        NAME_TO_CLASS.put(FlangName.FORMAT, Format.class);
        NAME_TO_CLASS.put(FlangName.STAR, Star.class);

        /// TYPEs
        NAME_TO_CLASS.put(FlangName.INTEGER_TYPE_SPEC, IntegerType.class);
        NAME_TO_CLASS.put(FlangName.LOGICAL, LogicalType.class);
    }

    public static boolean isClass(String type) {
        return FlangName.convertTry(type).map(NAME_TO_CLASS::containsKey).orElse(false);
    }

    public static Optional<Class<? extends FortranNode>> getClass(String type) {
        return FlangName.convertTry(type).map(NAME_TO_CLASS::get);
    }

}
