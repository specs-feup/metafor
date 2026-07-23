package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;
import pt.up.fe.specs.fortran.ast.nodes.alloc.StatVariable;
import pt.up.fe.specs.fortran.ast.nodes.decl.*;
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
        processors.put(FortranFile.class, p::fortranFile);
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
        processors.put(NamedParameter.class, d::namedParameter);
        processors.put(StarParameter.class, d::starParameter);

        var v = new VariableProcessor(data);
        processors.put(DataRef.class, v::dataRef);
        processors.put(DesignatorVariable.class, v::designatorVariable);

        var s = new StmtProcessors(data);
        processors.put(PrintStmt.class, s::printStmt);
        processors.put(FormatStmt.class, s::formatStmt);
        processors.put(TypeDeclarationStmt.class, s::typeDeclarationStmt);
        processors.put(AssignmentStmt.class, s::assignmentStmt);
        processors.put(StmtBlock.class, s::stmtBlock);
        processors.put(CompilerDirective.class, s::compilerDirective);
        processors.put(GotoStmt.class, s::gotoStmt);
        processors.put(StopStmt.class, s::stopStmt);
        processors.put(CommonStmt.class, s::commonStmt);
        processors.put(CommonBlock.class, s::commonBlock);
        processors.put(CommonBlockObject.class, s::commonBlockObject);

        processors.put(IfConstruct.class, s::ifConstruct);
        processors.put(IfThenStmt.class, s::ifThenStmt);
        processors.put(ElseIfBlock.class, s::elseIfBlock);
        processors.put(ElseIfStmt.class, s::elseIfStmt);
        processors.put(ElseBlock.class, s::elseBlock);
        processors.put(ElseStmt.class, s::elseStmt);
        processors.put(EndIfStmt.class, s::endIfStmt);
        processors.put(IfStmt.class, s::ifStmt);
        processors.put(DoConstruct.class, s::doConstruct);
        processors.put(DoStmt.class, s::doStmt);
        processors.put(EndDoStmt.class, s::endDoStmt);

        processors.put(CaseConstruct.class, s::caseConstruct);
        processors.put(SelectCaseStmt.class, s::selectCaseStmt);
        processors.put(CaseBlock.class, s::caseBlock);
        processors.put(EndSelectStmt.class, s::endSelectStmt);

        processors.put(CallStmt.class, s::callStmt);
        processors.put(UseRenameStmt.class, s::useRenameStmt);
        processors.put(UseOnlyStmt.class, s::useOnlyStmt);
        processors.put(UseName.class, s::useName);
        processors.put(NamesRename.class, s::namesRename);
        processors.put(WriteStmt.class, s::writeStmt);
        processors.put(ContainsStmt.class, s::containsStmt);
        processors.put(AllocateStmt.class, s::allocateStmt);
        processors.put(DeallocateStmt.class, s::deallocateStmt);
        processors.put(ContinueStmt.class, s::continueStmt);
        processors.put(ParameterStmt.class, s::parameterStmt);
        processors.put(NamedConstantDef.class, s::namedConstantDef);

        processors.put(DataStmt.class, s::dataStmt);
        processors.put(DataStmtSet.class, s::dataStmtSet);
        processors.put(ProgramStmt.class, s::programStmt);
        processors.put(EndProgramStmt.class, s::endProgramStmt);
        processors.put(SubroutineStmt.class, s::subroutineStmt);
        processors.put(EndSubroutineStmt.class, s::endSubroutineStmt);
        processors.put(ReturnStmt.class, s::returnStmt);
        processors.put(DimensionStmt.class, s::dimensionStmt);
        processors.put(DimensionDecl.class, s::dimensionDecl);

        var e = new ExprProcessors(data);
        processors.put(StringLiteral.class, e::stringLiteral);
        processors.put(IntLiteral.class, e::intLiteral);
        processors.put(LogicalLiteral.class, e::logicalLiteral);
        processors.put(RealLiteral.class, e::realLiteral);
        processors.put(NamedLiteral.class, e::namedLiteral);
        processors.put(ParenExpr.class, e::parenExpr);
        processors.put(UnaryOperator.class, e::unaryOperator);
        processors.put(BinaryOperator.class, e::binaryOperator);
        processors.put(ArrayConstructor.class, e::arrayConstructor);
        processors.put(AcSpecification.class, e::acSpecification);
        processors.put(ArraySubscriptExpr.class, e::arraySubscriptExpr);
        processors.put(SubscriptTriplet.class, e::subscriptTriplet);
        processors.put(Call.class, e::call);
        processors.put(Argument.class, e::argument);
        processors.put(AcImpliedDo.class, e::acImpliedDo);
        processors.put(AcImpliedDoControl.class, e::acImpliedDoControl);
        processors.put(IntComplexPart.class, e::intComplexPart);
        processors.put(RealComplexPart.class, e::realComplexPart);
        processors.put(NamedComplexPart.class, e::namedComplexPart);
        processors.put(ComplexLiteral.class, e::complexLiteral);
        processors.put(Substring.class, e::substring);

        var t = new TypeProcessors(data);
        processors.put(IntegerType.class, t::integerType);
        processors.put(KindSelector.class, t::kindSelector);
        processors.put(LogicalType.class, t::logicalType);
        processors.put(DoublePrecisionType.class, t::doublePrecisionType);
        processors.put(CharacterType.class, t::characterType);
        processors.put(RealType.class, t::realType);
        processors.put(LengthSelector.class, t::lengthSelector);
        processors.put(ComplexType.class, t::complexType);

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

        var omp = new OmpProcessors(data, s);
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
