package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.ast.nodes.expr.BinaryOperator;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.LogicalLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.ParenExpr;
import pt.up.fe.specs.fortran.ast.nodes.expr.StringLiteral;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.MainProgram;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.ast.nodes.type.IntegerType;
import pt.up.fe.specs.fortran.ast.nodes.type.LogicalType;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.AllocatableKeyword;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.KeywordAttributeSpecifier;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.DeferredShapeSpecList;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.ExplicitShapeSpecification;
import pt.up.fe.specs.fortran.ast.nodes.utils.Format;
import pt.up.fe.specs.fortran.ast.nodes.utils.Star;

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
        NAME_TO_CLASS.put(FlangName.DO_CONSTRUCT, DoStmt.class);

        NAME_TO_CLASS.put(FlangName.IF_CONSTRUCT, IfConstruct.class);
        NAME_TO_CLASS.put(FlangName.IF_THEN_STMT, IfThenStmt.class);
        NAME_TO_CLASS.put(FlangName.ELSE_IF_BLOCK, ElseIfBlock.class);
        NAME_TO_CLASS.put(FlangName.ELSE_IF_STMT, ElseIfStmt.class);
        NAME_TO_CLASS.put(FlangName.ELSE_BLOCK, ElseBlock.class);
        NAME_TO_CLASS.put(FlangName.ELSE_STMT, ElseStmt.class);
        NAME_TO_CLASS.put(FlangName.END_IF_STMT, EndIfStmt.class);
        NAME_TO_CLASS.put(FlangName.IF_STMT, IfStmt.class);

        /// Variables
        NAME_TO_CLASS.put(FlangName.DATA_REF, DataRef.class);

        /// EXPRs
        NAME_TO_CLASS.put(FlangName.CHAR_LITERAL_CONSTANT, StringLiteral.class);
        NAME_TO_CLASS.put(FlangName.INT_LITERAL_CONSTANT, IntLiteral.class);
        NAME_TO_CLASS.put(FlangName.LOGICAL_LITERAL_CONSTANT, LogicalLiteral.class);
        NAME_TO_CLASS.put(FlangName.FORMAT, Format.class);
        NAME_TO_CLASS.put(FlangName.STAR, Star.class);
        NAME_TO_CLASS.put(FlangName.PARENTHESES, ParenExpr.class);
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
        NAME_TO_CLASS.put(FlangName.ARRAY_CONSTRUCTOR, ArrayConstructor.class);
        NAME_TO_CLASS.put(FlangName.AC_SPEC, AcSpecification.class);

        /// TYPEs
        NAME_TO_CLASS.put(FlangName.INTEGER_TYPE_SPEC, IntegerType.class);
        NAME_TO_CLASS.put(FlangName.LOGICAL, LogicalType.class);

        ///  LOOP
        NAME_TO_CLASS.put(FlangName.LOOP_BOUNDS, RangeLoopControl.class);
        NAME_TO_CLASS.put(FlangName.CONCURRENT, ConcurrentLoopControl.class);
        NAME_TO_CLASS.put(FlangName.CONCURRENT_CONTROL, ConcurrentRange.class);

        ///  ATTRIBUTES
        NAME_TO_CLASS.put(FlangName.ARRAY_SPEC, ArraySpecification.class);
        NAME_TO_CLASS.put(FlangName.ALLOCATABLE, AllocatableKeyword.class);

        ///  SHAPES
        NAME_TO_CLASS.put(FlangName.EXPLICIT_SHAPE_SPEC, ExplicitShapeSpecification.class);
        NAME_TO_CLASS.put(FlangName.DEFERRED_SHAPE_SPEC_LIST, DeferredShapeSpecList.class);

        /// OPENMP
        NAME_TO_CLASS.put(FlangName.OPENMP_BLOCK_CONSTRUCT, OmpBlockConstruct.class);
        NAME_TO_CLASS.put(FlangName.OPENMP_LOOP_CONSTRUCT, OmpLoopConstruct.class);
        NAME_TO_CLASS.put(FlangName.PUBLIC, OmpDataSharingClause.class);
        NAME_TO_CLASS.put(FlangName.PRIVATE, OmpDataSharingClause.class);
        NAME_TO_CLASS.put(FlangName.FIRST_PRIVATE, OmpDataSharingClause.class);

    }

    public static boolean isClass(String type) {
        return FlangName.convertTry(type).map(NAME_TO_CLASS::containsKey).orElse(false);
    }

    public static Optional<Class<? extends FortranNode>> getClass(String type) {
        return FlangName.convertTry(type).map(NAME_TO_CLASS::get);
    }

}
