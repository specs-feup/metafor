package pt.up.fe.specs.fortran.ast;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.ExprInitialization;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.ListInitialization;
import pt.up.fe.specs.fortran.ast.nodes.decl.NamedParameter;
import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.io.StarFormat;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpOrderedClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpReductionClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpClauseKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.EndProgramStmt;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.MainProgram;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.ProgramStmt;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.ProgramUnit;
import pt.up.fe.specs.fortran.ast.nodes.stmt.LiteralExecutionStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.PrintStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.EndDoStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.UseOnlyStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.UseRenameStmt;
import pt.up.fe.specs.fortran.ast.nodes.io.Format;
import pt.up.fe.specs.fortran.ast.nodes.utils.LabelRef;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsCollections;

import java.util.*;

public class FortranNodeFactory {

    private final FortranContext context;
    private final DataStore baseData;

    public FortranNodeFactory(FortranContext context, DataStore baseData) {
        this.context = context;
        this.baseData = baseData;
    }

    public FortranNodeFactory(FortranContext context) {
        this(context, null);
    }

    public FortranNodeFactory(FortranNodeFactory factory) {
        this(factory.context, factory.baseData);
    }

    public DataStore newDataStore(Class<? extends FortranNode> nodeClass) {
        return newDataStore(nodeClass, null);
    }

    public DataStore newDataStore(Class<? extends FortranNode> nodeClass, String id) {

        DataStore data = DataStore.newInstance(StoreDefinitions.fromInterface(nodeClass), true);

        // Add base node, if present
        if (baseData != null) {
            data.addAll(baseData);
        }

        // Set context
        data.set(FortranNode.CONTEXT, context);

        // Set id
        if (id != null) {
            data.set(FortranNode.ID, id);
        } else {
            data.set(FortranNode.ID, context.get(FortranContext.ID_GENERATOR).next("node_"));
        }


        return data;
    }

    @Override
    public String toString() {
        return "FortranNodeFactory " + hashCode();
    }

    /**
     * Creates an empty node of the given class.
     *
     * @param <T>
     * @param nodeClass
     * @return
     */
    public <T extends FortranNode> T newNode(Class<T> nodeClass) {
        return newNode(nodeClass, Collections.emptyList());
    }

    public <T extends FortranNode> T newNode(Class<T> nodeClass, Collection<? extends FortranNode> children) {
        return newNode(nodeClass, children, null);
    }

    public <T extends FortranNode> T newNode(Class<T> nodeClass, Collection<? extends FortranNode> children, String id) {
        try {
            DataStore data = newDataStore(nodeClass, id);

            return nodeClass.getDeclaredConstructor(DataStore.class, Collection.class)
                    .newInstance(data, children);
        } catch (Exception e) {
            throw new RuntimeException("Could not create FortranNode", e);
        }
    }

    // PROGRAM

    public Application application(List<FortranFile> files) {
        DataStore data = newDataStore(Application.class);
        return new Application(data, files);
    }

    public FortranFile fortranFile(List<ProgramUnit> units) {
        DataStore data = newDataStore(FortranFile.class);
        data.set(FortranFile.FINAL_COMMENTS, List.of());

        return new FortranFile(data, units);
    }

    private void initComments(DataStore data) {
        data.set(Stmt.LEADING_COMMENTS, List.of());
    }

    private void initComments(Stmt stmt) {
        stmt.set(Stmt.LEADING_COMMENTS, List.of());
    }

    public MainProgram mainProgram(String programName, List<FortranNode> execution) {
        DataStore data = newDataStore(MainProgram.class);
        data.set(MainProgram.NAME, Optional.ofNullable(programName));

        var programStmt = newNode(ProgramStmt.class, Collections.emptyList());
        initComments(programStmt);
        var executionBlock = execution(execution);
        var endProgramStmt = newNode(EndProgramStmt.class, Collections.emptyList());
        initComments(endProgramStmt);

        return new MainProgram(data, List.of(programStmt, executionBlock, endProgramStmt));
    }

    public Execution execution(List<FortranNode> statements) {
        DataStore data = newDataStore(Execution.class);

        return new Execution(data, statements);
    }

    // STMT

    public PrintStmt printStmt(Format format, FortranNode... outputItems) {
        return printStmt(format, List.of(outputItems));
    }

    public PrintStmt printStmt(Format format, List<FortranNode> outputItems) {
        DataStore data = newDataStore(PrintStmt.class);
        initComments(data);

        return new PrintStmt(data, SpecsCollections.concat(format, outputItems));
    }

    public LiteralExecutionStmt literalExecutionStmt(String code) {
        DataStore data = newDataStore(LiteralExecutionStmt.class);
        data.set(LiteralExecutionStmt.LITERAL_CODE, code);
        initComments(data);

        return new LiteralExecutionStmt(data, Collections.emptyList());
    }

    // DECL
    public LabelDecl labelDecl(int label) {
        SpecsCheck.checkArgument(label >= 1 && label <= 99999, () -> "Expected label to be between 1 and 99999, is " + label);

        DataStore data = newDataStore(LabelDecl.class);
        data.set(LabelDecl.LABEL, label);

        return new LabelDecl(data, Collections.emptyList());
    }

    public ExprInitialization exprInitialization() {
        DataStore data = newDataStore(ExprInitialization.class);

        return new ExprInitialization(data, Collections.emptyList());
    }

    public ListInitialization listInitialization() {
        DataStore data = newDataStore(ListInitialization.class);

        return new ListInitialization(data, Collections.emptyList());
    }

    // EXPR

    public StringLiteral stringLiteral(String literal) {
        return stringLiteral(literal, null);
    }

    public StringLiteral stringLiteral(String literal, String kindParam) {
        DataStore data = newDataStore(StringLiteral.class);
        data.set(StringLiteral.CONTENTS, literal);

        return new StringLiteral(data, Collections.emptyList());
    }

    // UTIL

    public StarFormat starFormat() {
        DataStore data = newDataStore(Format.class);
        return new StarFormat(data, List.of());
    }

    public LabelRef labelRef(LabelDecl labelDecl) {
        DataStore data = newDataStore(LabelRef.class);

        data.set(LabelRef.LABEL_DECL, labelDecl);

        return new LabelRef(data, Collections.emptyList());
    }

    public OmpLoopConstruct ompLoopConstruct(DoConstruct doConstruct, List<OmpClause> clauses) {
        DataStore data = newDataStore(OmpLoopConstruct.class);

        OmpLoopConstruct newNode = new OmpLoopConstruct(data, clauses);

        newNode.addChild(doConstruct);

        return newNode;
    }

    public OmpLoopConstruct emptyOmpLoopConstruct() {
        DataStore data = newDataStore(OmpLoopConstruct.class);

        OmpLoopConstruct newNode = new OmpLoopConstruct(data, Collections.emptyList());

        newNode.set(OmpLoopConstruct.KINDS, OmpDirectiveKind.getKinds("parallel do"));

        return newNode;
    }

    public OmpBlockConstruct emptyOmpBlockConstruct() {
        DataStore data = newDataStore(OmpBlockConstruct.class);

        OmpBlockConstruct newNode = new OmpBlockConstruct(data, Collections.emptyList());

        newNode.set(OmpBlockConstruct.KINDS, OmpDirectiveKind.getKinds("parallel"));

        return newNode;
    }

    public OmpDataSharingClause ompDataSharingClause(OmpClauseKind kind, List<DataRef> refs) {
        DataStore data = newDataStore(OmpDataSharingClause.class);

        OmpDataSharingClause newNode = new OmpDataSharingClause(data, refs);

        newNode.set(OmpDataSharingClause.KIND, kind);

        return newNode;
    }

    public DataRef dataRef(String name) {
        DataStore data = newDataStore(DataRef.class);

        DataRef newNode = new DataRef(data, Collections.emptyList());

        newNode.set(DataRef.NAME, name);

        return newNode;
    }

    public UseRenameStmt useRenameStmt(String moduleName) {
        DataStore data = newDataStore(UseRenameStmt.class);

        UseRenameStmt newNode = new UseRenameStmt(data, Collections.emptyList());

        newNode.set(UseRenameStmt.NAME, moduleName);

        return newNode;
    }

    public UseOnlyStmt useOnlyStmt(String moduleName) {
        DataStore data = newDataStore(UseOnlyStmt.class);

        UseOnlyStmt newNode = new UseOnlyStmt(data, Collections.emptyList());

        newNode.set(UseOnlyStmt.NAME, moduleName);

        return newNode;
    }

    public OmpReductionClause ompReductionClause(BinaryOperatorKind operator, List<DataRef> refs) {
        DataStore data = newDataStore(OmpReductionClause.class);

        OmpReductionClause newNode = new OmpReductionClause(data, refs);

        newNode.set(OmpReductionClause.KIND, OmpClauseKind.REDUCTION);

        newNode.set(OmpReductionClause.OPERATOR, operator);

        return newNode;
    }

    public IntLiteral intLiteral(int value) {
        DataStore data = newDataStore(IntLiteral.class);

        IntLiteral newNode = new IntLiteral(data, Collections.emptyList());
        newNode.set(IntLiteral.SOURCE, Integer.toString(value));

        return newNode;
    }

    public RangeLoopControl rangeLoopControl(DataRef var, Expr lower, Expr upper) {
        DataStore data = newDataStore(RangeLoopControl.class);
        return new RangeLoopControl(data, Arrays.asList(var, lower, upper));
    }

    public DoConstruct doConstruct(LoopControl control) {
        DataStore data = newDataStore(DoConstruct.class);

        DoStmt doStmt = new DoStmt(data, List.of(control));
        Execution body = execution(Collections.emptyList());
        EndDoStmt endDoStmt = new EndDoStmt(data, Collections.emptyList());

        return new DoConstruct(data, List.of(doStmt, body, endDoStmt));
    }

    public Argument argument(Expr expr) {
        DataStore data = newDataStore(Argument.class);
        return new Argument(data, Collections.singletonList(expr));
    }

    public Call functionCall(DataRef callee, List<Argument> args) {
        DataStore data = newDataStore(Call.class);
        List<FortranNode> children = new ArrayList<>(args.size() + 1);
        children.add(callee);
        children.addAll(args);
        return new Call(data, children);
    }

    public BinaryOperator binaryOperator(BinaryOperatorKind kind, Expr lhs, Expr rhs) {
        DataStore data = newDataStore(BinaryOperator.class);
        BinaryOperator node = new BinaryOperator(data, Arrays.asList(lhs, rhs));
        node.set(BinaryOperator.OP, kind);
        return node;
    }

    public OmpOrderedClause ompOrderedClause(int value) {
        DataStore data = newDataStore(OmpOrderedClause.class);

        OmpOrderedClause newNode = new OmpOrderedClause(data, Collections.emptyList());

        newNode.set(OmpOrderedClause.KIND, OmpClauseKind.ORDERED);

        newNode.addChild(intLiteral(value));

        return newNode;
    }

    public NamedParameter namedParameter(String name) {
        var data = newDataStore(NamedParameter.class);

        var node = new NamedParameter(data, Collections.emptyList());
        node.set(NamedParameter.NAME, name);

        return node;
    }
}
