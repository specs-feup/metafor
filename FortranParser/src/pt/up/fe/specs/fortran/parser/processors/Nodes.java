package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;
import pt.up.fe.specs.fortran.ast.nodes.alloc.ExprAllocOption;
import pt.up.fe.specs.fortran.ast.nodes.alloc.VarAllocOption;
import pt.up.fe.specs.fortran.ast.nodes.decl.*;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.*;
import pt.up.fe.specs.fortran.ast.nodes.specification.funcspec.DeclTypeFunctionSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.funcspec.EmptyFunctionSpec;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DerivedDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.IntrinsicDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.StarDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.ParamCharLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.DeferredTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.ExprTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.StarTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.io.*;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpNowaitClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpReductionClause;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.*;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.*;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.Module;
import pt.up.fe.specs.fortran.ast.nodes.specification.*;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.NameGenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.OpGenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.OtherGenericSpec;
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
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.NamesRename;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.UseName;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.UseOnlyStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.UseRenameStmt;
import pt.up.fe.specs.fortran.ast.nodes.type.*;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.IntentSpec;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.KeywordAttributeSpecifier;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.ConstLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.KindParamLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.ParamLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.TypeParam;
import pt.up.fe.specs.fortran.ast.nodes.utils.IoUnit;
import pt.up.fe.specs.fortran.ast.nodes.utils.NameValue;
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
        processors.put(SubprogramUnit.class, p::subprogramUnit);
        processors.put(Specification.class, p::specification);
        processors.put(Execution.class, p::execution);
        processors.put(Subroutine.class, p::subroutine);
        processors.put(Function.class, p::function);
        processors.put(InternalSubprogramPart.class, p::internalSubprogramPart);
        processors.put(Module.class, p::module);
        processors.put(ModuleSubprogramPart.class, p::moduleSubprogramPart);

        var alloc = new AllocProcessors(data);
        processors.put(Allocation.class, alloc::allocation);
        processors.put(ExprAllocOption.class, alloc::exprAllocOption);
        processors.put(VarAllocOption.class, alloc::varAllocOption);

        var d = new DeclProcessors(data);
        processors.put(EntityDecl.class, d::entityDecl);
        processors.put(DataStmtValue.class, d::dataStmtValue);
        processors.put(NamedParameter.class, d::namedParameter);
        processors.put(StarParameter.class, d::starParameter);
        processors.put(ExprTypeParamValue.class, d::exprTypeParamValue);
        processors.put(StarTypeParamValue.class, d::starTypeParamValue);
        processors.put(DeferredTypeParamValue.class, d::deferredTypeParamValue);
        processors.put(NamedOperator.class, d::namedOperator);
        processors.put(IntrinsicOperator.class, d::intrinsicOperator);
        processors.put(NameGenericSpec.class, d::nameGenericSpec);
        processors.put(OpGenericSpec.class, d::opGenericSpec);
        processors.put(OtherGenericSpec.class, d::otherGenericSpec);
        processors.put(LetterSpec.class, d::letterSpec);
        processors.put(ImplicitSpec.class, d::implicitSpec);
        processors.put(DeclTypeFunctionSpec.class, d::declTypeFunctionSpec);
        processors.put(EmptyFunctionSpec.class, d::emptyFunctionSpec);

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
        processors.put(ContainsStmt.class, s::containsStmt);
        processors.put(AllocateStmt.class, s::allocateStmt);
        processors.put(DeallocateStmt.class, s::deallocateStmt);
        processors.put(ContinueStmt.class, s::continueStmt);
        processors.put(ParameterStmt.class, s::parameterStmt);
        processors.put(ExternalStmt.class, s::externalStmt);
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
        processors.put(NamelistStmt.class, s::namelistStmt);
        processors.put(NamelistGroup.class, s::namelistGroup);
        processors.put(FunctionStmt.class, s::functionStmt);
        processors.put(LanguageBindingSpec.class, s::languageBindingSpec);
        processors.put(EndFunctionStmt.class, s::endFunctionStmt);
        processors.put(ModuleStmt.class, s::moduleStmt);
        processors.put(EndModuleStmt.class, s::endModuleStmt);
        processors.put(AccessStmt.class, s::accessStmt);
        processors.put(ImportStmt.class, s::importStmt);
        processors.put(DefaultImplicitStmt.class, s::defaultImplicitStmt);
        processors.put(ImplicitNoneStmt.class, s::implicitNoneStmt);

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
        processors.put(ConstLenSelector.class, t::constLenSelector);
        processors.put(ParamCharLenSelector.class, t::paramCharLenSelector);
        processors.put(ParamLenSelector.class, t::paramLenSelector);
        processors.put(KindParamLenSelector.class, t::kindParamLenSelector);
        processors.put(RealType.class, t::realType);
        processors.put(ComplexType.class, t::complexType);
        processors.put(DerivedType.class, t::derivedType);
        processors.put(TypeParam.class, t::typeParam);
        processors.put(IntrinsicDeclType.class, t::intrinsicDeclType);
        processors.put(DerivedDeclType.class, t::derivedDeclType);
        processors.put(StarDeclType.class, t::starDeclType);

        var a = new AttributesProcessor(data);
        processors.put(ArraySpec.class, a::arraySpecification);
        processors.put(KeywordAttributeSpecifier.class, a::keywordSpecifier);
        processors.put(IntentSpec.class, a::intentSpec);
        processors.put(NamedConstantDef.class, a::namedConstantDef);

        var shapes = new ShapesProcessor(data);
        processors.put(ExplicitShape.class, shapes::explicitShape);
        processors.put(AssumedShape.class, shapes::assumedShape);
        processors.put(AssumedImpliedShape.class, shapes::assumedImpliedShape);
        processors.put(ExplicitShapeArraySpec.class, shapes::explicitShapeArraySpec);
        processors.put(AssumedShapeArraySpec.class, shapes::assumedShapeArraySpec);
        processors.put(DeferredShapeSpec.class, shapes::deferredShapeSpec);
        processors.put(AssumedSizeSpec.class, shapes::assumedSizeSpec);
        processors.put(ImpliedShapeSpec.class, shapes::impliedShapeSpec);
        processors.put(AssumedRankSpec.class, shapes::assumedRankSpec);

        var u = new UtilsProcessors(data);
        processors.put(NameValue.class, u::nameValue);
        processors.put(IoUnit.class, u::ioUnit);
        processors.put(ExprIoControlSpec.class, u::ioControlSpec);

        var l = new LoopProcessors(data);
        processors.put(RangeLoopControl.class, l::loopRange);
        processors.put(ConcurrentLoopControl.class, l::concurrentLoopControl);
        processors.put(ConcurrentRange.class, l::concurrentRange);

        var i = new IoProcessors(data, s);
        processors.put(OpenStmt.class, i::openStmt);
        processors.put(ExprConnectSpec.class, i::exprConnectSpec);
        processors.put(VarConnectSpec.class, i::varConnectSpec);
        processors.put(ErrConnectSpec.class, i::errConnectSpec);
        processors.put(ExprIoControlSpec.class, i::exprIoControlSpec);
        processors.put(VarIoControlSpec.class, i::varIoControlSpec);
        processors.put(LabelIoControlSpec.class, i::labelIoControlSpec);
        processors.put(StarUnitIoControlSpec.class, i::starUnitIoControlSpec);
        processors.put(FormatIoControlSpec.class, i::formatIoControlSpec);
        processors.put(NamelistIoControlSpec.class, i::namelistIoControlSpec);
        processors.put(RewindStmt.class, i::rewindStmt);
        processors.put(UnitPosFlushSpec.class, i::unitPosFlushSpec);
        processors.put(VarPosFlushSpec.class, i::varPosFlushSpec);
        processors.put(ErrPosFlushSpec.class, i::errPosFlushSpec);
        processors.put(ReadStmt.class, i::readStmt);
        processors.put(VarInputItem.class, i::varInputItem);
        processors.put(WriteStmt.class, i::writeStmt);
        processors.put(ExprOutputItem.class, i::exprOutputItem);
        processors.put(WaitStmt.class, i::waitStmt);
        processors.put(ExprWaitSpec.class, i::exprWaitSpec);
        processors.put(VarWaitSpec.class, i::varWaitSpec);
        processors.put(LabelWaitSpec.class, i::labelWaitSpec);
        processors.put(CloseStmt.class, i::closeStmt);
        processors.put(ExprCloseSpec.class, i::exprCloseSpec);
        processors.put(VarCloseSpec.class, i::varCloseSpec);
        processors.put(ErrCloseSpec.class, i::errCloseSpec);
        processors.put(ExprFormat.class, i::exprFormat);
        processors.put(LabelFormat.class, i::labelFormat);
        processors.put(StarFormat.class, i::starFormat);

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
