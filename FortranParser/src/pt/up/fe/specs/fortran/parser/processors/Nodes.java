package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.LogicalLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.StringLiteral;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.MainProgram;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.AssignmentStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.FormatStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.PrintStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.TypeDeclarationStmt;
import pt.up.fe.specs.fortran.ast.nodes.type.IntegerType;
import pt.up.fe.specs.fortran.ast.nodes.type.LogicalType;
import pt.up.fe.specs.fortran.ast.nodes.utils.Format;
import pt.up.fe.specs.fortran.ast.nodes.utils.Star;
import pt.up.fe.specs.fortran.ast.nodes.variable.DataRef;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;
import pt.up.fe.specs.util.classmap.ConsumerClassMap;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Maps node classes to processors for each class, which will populate each FortranAst node.
 */
public class Nodes {

    private final ConsumerClassMap<FortranNode> processors;

    public Nodes(FortranJsonResult data) {
        this.processors = new ConsumerClassMap<>();

        var p = new ProgramProcessors(data);
        processors.put(FortranFile.class, p::program);
        processors.put(MainProgram.class, p::mainProgram);
        processors.put(Specification.class, p::specification);
        processors.put(Execution.class, p::execution);


        var d = new DeclProcessors(data);
        processors.put(EntityDecl.class, d::entityDecl);

        var v = new VariableProcessor(data);
        processors.put(DataRef.class, v::dataRefProcessor);

        var s = new StmtProcessors(data);
        processors.put(PrintStmt.class, s::printStmt);
        processors.put(FormatStmt.class, s::formatStmt);
        processors.put(TypeDeclarationStmt.class, s::typeDeclarationStmt);
        processors.put(AssignmentStmt.class, s::assignmentStmt);

        var e = new ExprProcessors(data);
        processors.put(StringLiteral.class, e::stringLiteral);
        processors.put(IntLiteral.class, e::intLiteral);
        processors.put(LogicalLiteral.class, e::logicalLiteral);

        var t = new TypeProcessors(data);
        processors.put(IntegerType.class, t::integerType);
        processors.put(LogicalType.class, t::logicalType);

        var u = new UtilsProcessors(data);
        processors.put(Star.class, u::star);
        processors.put(Format.class, u::format);
    }

    public void process(FortranNode node) {
        try {
            processors.accept(node);
        } catch (NotImplementedException e) {
            throw new RuntimeException("Could not find a processor for node of class '" + node.getClass() + "', please add a mapping in class " + Nodes.class + ".");
        }

    }

}
