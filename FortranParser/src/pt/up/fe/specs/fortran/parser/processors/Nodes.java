package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.DummyArgumentDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.ast.nodes.expr.BinaryOperator;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.LogicalLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.StringLiteral;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.MainProgram;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.MainProgram;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.AssignmentStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.FormatStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.PrintStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.TypeDeclarationStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.CaseBlock;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.CaseConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.EndSelectStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.SelectCaseStmt;
import pt.up.fe.specs.fortran.ast.nodes.type.CharacterType;
import pt.up.fe.specs.fortran.ast.nodes.type.DoublePrecisionType;
import pt.up.fe.specs.fortran.ast.nodes.type.IntegerType;
import pt.up.fe.specs.fortran.ast.nodes.type.LogicalType;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.KeywordAttributeSpecifier;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.DeferredShapeSpecList;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.ExplicitShapeSpecification;
import pt.up.fe.specs.fortran.ast.nodes.utils.Format;
import pt.up.fe.specs.fortran.ast.nodes.utils.NameValue;
import pt.up.fe.specs.fortran.ast.nodes.utils.Star;
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
        processors.put(Subroutine.class, p::subroutine);
        processors.put(InternalSubprogram.class, p::internalSubprogram);


        var d = new DeclProcessors(data);
        processors.put(EntityDecl.class, d::entityDecl);
        processors.put(DummyArgumentDecl.class, d::dummyArgumentDecl);

        var v = new VariableProcessor(data);
        processors.put(DataRef.class, v::dataRefProcessor);

        var s = new StmtProcessors(data);
        processors.put(PrintStmt.class, s::printStmt);
        processors.put(FormatStmt.class, s::formatStmt);
        processors.put(TypeDeclarationStmt.class, s::typeDeclarationStmt);
        processors.put(AssignmentStmt.class, s::assignmentStmt);
        processors.put(StmtBlock.class, s::stmtBlock);
        processors.put(CompilerDirective.class, s::compilerDirective);

        processors.put(IfConstruct.class, s::ifConstruct);
        processors.put(IfThenStmt.class, s::ifThenStmt);
        processors.put(ElseIfBlock.class, s::elseIfBlock);
        processors.put(ElseIfStmt.class, s::elseIfStmt);
        processors.put(ElseBlock.class, s::elseBlock);
        processors.put(ElseStmt.class, s::elseStmt);
        processors.put(EndIfStmt.class, s::endIfStmt);
        processors.put(IfStmt.class, s::ifStmt);
        processors.put(DoStmt.class, s::doStmt);

        processors.put(CaseConstruct.class, s::caseConstruct);
        processors.put(SelectCaseStmt.class, s::selectCaseStmt);
        processors.put(CaseBlock.class, s::caseBlock);
        processors.put(EndSelectStmt.class, s::endSelectStmt);

        processors.put(CallStmt.class, s::callStmt);

        var e = new ExprProcessors(data);
        processors.put(StringLiteral.class, e::stringLiteral);
        processors.put(IntLiteral.class, e::intLiteral);
        processors.put(LogicalLiteral.class, e::logicalLiteral);
        processors.put(RealLiteral.class, e::realLiteral);
        processors.put(ParenExpr.class, e::parenExpr);
        processors.put(BinaryOperator.class, e::binaryOperator);
        processors.put(ArrayConstructor.class, e::arrayConstructor);
        processors.put(AcSpecification.class, e::acSpecification);
        processors.put(ArraySubscriptExpr.class, e::arraySubscriptExpr);
        processors.put(Call.class, e::call);
        processors.put(Argument.class, e::argument);
        processors.put(AcImpliedDo.class, e::acImpliedDo);
        processors.put(AcImpliedDoControl.class, e::acImpliedDoControl);

        var t = new TypeProcessors(data);
        processors.put(IntegerType.class, t::integerType);
        processors.put(LogicalType.class, t::logicalType);
        processors.put(DoublePrecisionType.class, t::doublePrecisionType);
        processors.put(CharacterType.class, t::characterType);

        var a = new AttributesProcessor(data);
        processors.put(ArraySpecification.class, a::arraySpecification);
        processors.put(KeywordAttributeSpecifier.class, a::keywordSpecifier);

        var shapes = new ShapesProcessor(data);
        processors.put(ExplicitShapeSpecification.class, shapes::explicitShapeSpec);
        processors.put(DeferredShapeSpecList.class, shapes::deferredShapeSpecLis);

        var u = new UtilsProcessors(data);
        processors.put(Star.class, u::star);
        processors.put(Format.class, u::format);
        processors.put(NameValue.class, u::nameValue);

        var l = new LoopProcessors(data);
        processors.put(RangeLoopControl.class, l::loopRange);
        processors.put(ConcurrentLoopControl.class, l::concurrentLoopControl);
        processors.put(ConcurrentRange.class, l::concurrentRange);
    }

    public void process(FortranNode node) {
        try {
            processors.accept(node);
        } catch (NotImplementedException e) {
            throw new RuntimeException("Could not find a processor for node of class '" + node.getClass() + "', please add a mapping in class " + Nodes.class + ".");
        }

    }

}
