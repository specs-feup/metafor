package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;
import pt.up.fe.specs.fortran.ast.nodes.alloc.StatVariable;
import pt.up.fe.specs.fortran.ast.nodes.decl.DataStmtValue;
import pt.up.fe.specs.fortran.ast.nodes.decl.DummyArgumentDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.KindSelector;
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
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.CaseBlock;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.CaseConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.EndSelectStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.SelectCaseStmt;
import pt.up.fe.specs.fortran.ast.nodes.type.*;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.IntentSpec;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.KeywordAttributeSpecifier;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.AllocateShapeSpecification;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.DeferredShapeSpecList;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.ExplicitShapeSpecification;
import pt.up.fe.specs.fortran.ast.nodes.utils.*;
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

        var alloc = new AllocProcessors(data);
        processors.put(Allocation.class, alloc::allocation);
        processors.put(StatVariable.class, alloc::statVariable);

        var d = new DeclProcessors(data);
        processors.put(EntityDecl.class, d::entityDecl);
        processors.put(DataStmtValue.class, d::dataStmtValue);
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
        processors.put(GotoStmt.class, s::gotoStmt);
        processors.put(StopStmt.class, s::stopStmt);

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
        processors.put(UseStmt.class, s::useStmt);
        processors.put(WriteStmt.class, s::writeStmt);
        processors.put(ContainsStmt.class, s::containsStmt);
        processors.put(AllocateStmt.class, s::allocateStmt);
        processors.put(DeallocateStmt.class, s::deallocateStmt);
        processors.put(ContinueStmt.class, s::continueStmt);
        processors.put(ParameterStmt.class, s::parameterStmt);
        processors.put(NamedConstantDef.class, s::namedConstantDef);

        var e = new ExprProcessors(data);
        processors.put(StringLiteral.class, e::stringLiteral);
        processors.put(IntLiteral.class, e::intLiteral);
        processors.put(LogicalLiteral.class, e::logicalLiteral);
        processors.put(RealLiteral.class, e::realLiteral);
        processors.put(ParenExpr.class, e::parenExpr);
        processors.put(UnaryOperator.class, e::unaryOperator);
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
        processors.put(KindSelector.class, t::kindSelector);
        processors.put(LogicalType.class, t::logicalType);
        processors.put(DoublePrecisionType.class, t::doublePrecisionType);
        processors.put(CharacterType.class, t::characterType);
        processors.put(RealType.class, t::realType);
        processors.put(LengthSelector.class, t::lengthSelector);

        var a = new AttributesProcessor(data);
        processors.put(ArraySpecification.class, a::arraySpecification);
        processors.put(KeywordAttributeSpecifier.class, a::keywordSpecifier);
        processors.put(IntentSpec.class, a::intentSpec);
        processors.put(NamedConstantDef.class, a::namedConstantDef);

        var shapes = new ShapesProcessor(data);
        processors.put(ExplicitShapeSpecification.class, shapes::explicitShapeSpec);
        processors.put(DeferredShapeSpecList.class, shapes::deferredShapeSpecLis);
        processors.put(AllocateShapeSpecification.class, shapes::allocateShapeSpec);

        var u = new UtilsProcessors(data);
        processors.put(Star.class, u::star);
        processors.put(Format.class, u::format);
        processors.put(NameValue.class, u::nameValue);
        processors.put(IoUnit.class, u::ioUnit);
        processors.put(IoControlSpec.class, u::ioControlSpec);

        var l = new LoopProcessors(data);
        processors.put(RangeLoopControl.class, l::loopRange);
        processors.put(ConcurrentLoopControl.class, l::concurrentLoopControl);
        processors.put(ConcurrentRange.class, l::concurrentRange);

        var omp = new OmpProcessors(data);
        processors.put(OmpBlockConstruct.class, omp::ompBlockConstruct);
        processors.put(OmpLoopConstruct.class, omp::ompLoopConstruct);
        processors.put(OmpDataSharingClause.class, omp::ompDataSharingClause);
        processors.put(OmpReductionClause.class, omp::ompReductionClause);
        processors.put(OmpNowaitClause.class, omp::ompNowaitClause);
    }

    public void process(FortranNode node) {
        try {
            processors.accept(node);
        } catch (NotImplementedException e) {
            throw new RuntimeException("Could not find a processor for node of class '" + node.getClass() + "', please add a mapping in class " + Nodes.class + ".");
        }

    }

}
