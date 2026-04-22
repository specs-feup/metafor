package pt.up.fe.specs.fortran.ast;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.ast.nodes.expr.Literal;
import pt.up.fe.specs.fortran.ast.nodes.expr.StringLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpReductionClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpClauseKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.utils.Format;
import pt.up.fe.specs.fortran.ast.nodes.utils.LabelRef;
import pt.up.fe.specs.fortran.ast.nodes.utils.Star;
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
        return new FortranFile(data, units);
    }


    public MainProgram mainProgram(String programName, List<ExecutableStmt> execution) {
        DataStore data = newDataStore(MainProgram.class);
        data.set(MainProgram.PROGRAM_NAME, Optional.ofNullable(programName));

        var executionBlock = execution(execution);

        return new MainProgram(data, List.of(executionBlock));
    }

    public Execution execution(List<ExecutableStmt> statements) {
        DataStore data = newDataStore(Execution.class);

        return new Execution(data, statements);
    }

    // STMT

    public PrintStmt printStmt(Format format, FortranNode... outputItems) {
        return printStmt(format, List.of(outputItems));
    }

    public PrintStmt printStmt(Format format, List<FortranNode> outputItems) {
        DataStore data = newDataStore(PrintStmt.class);

        return new PrintStmt(data, SpecsCollections.concat(format, outputItems));
    }

    public LiteralExecutionStmt literalExecutionStmt(String code) {
        DataStore data = newDataStore(LiteralExecutionStmt.class);
        data.set(LiteralExecutionStmt.LITERAL_CODE, code);

        return new LiteralExecutionStmt(data, Collections.emptyList());
    }

    // DECL
    public LabelDecl labelDecl(int label) {
        SpecsCheck.checkArgument(label >= 1 && label <= 99999, () -> "Expected label to be between 1 and 99999, is " + label);

        DataStore data = newDataStore(LabelDecl.class);
        data.set(LabelDecl.LABEL, label);

        return new LabelDecl(data, Collections.emptyList());
    }

    // EXPR

    public StringLiteral stringLiteral(String literal) {
        return stringLiteral(literal, null);
    }

    public StringLiteral stringLiteral(String literal, String kindParam) {
        DataStore data = newDataStore(StringLiteral.class);
        data.set(Literal.SOURCE_LITERAL, literal);

        return new StringLiteral(data, Collections.emptyList());
    }

    // UTIL

    public Format format(FortranNode formatType) {
        // Check if node is of allowed type
        if (!(formatType instanceof Star)) {
            throw new RuntimeException("Unsupported type for Format child: " + formatType.getClass());
        }

        DataStore data = newDataStore(Format.class);
        return new Format(data, List.of(formatType));
    }

    public Star star() {
        DataStore data = newDataStore(Star.class);
        return new Star(data, Collections.emptyList());
    }

    public LabelRef labelRef(LabelDecl labelDecl) {
        DataStore data = newDataStore(LabelRef.class);

        data.set(LabelRef.LABEL_DECL, labelDecl);

        return new LabelRef(data, Collections.emptyList());
    }

    public OmpLoopConstruct ompLoopConstruct(DoStmt doStmt, List<OmpClause> clauses) {
        DataStore data = newDataStore(OmpLoopConstruct.class);

        OmpLoopConstruct newNode = new OmpLoopConstruct(data, clauses);

        newNode.addChild(doStmt);

        return newNode;
    }

    public OmpLoopConstruct emptyOmpLoopConstruct() {
        DataStore data = newDataStore(OmpLoopConstruct.class);

        OmpLoopConstruct newNode = new OmpLoopConstruct(data, Collections.emptyList());

        newNode.set(OmpLoopConstruct.KINDS, OmpDirectiveKind.getKinds("parallel do"));

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

    public UseStmt useStmt(String moduleName) {
        DataStore data = newDataStore(UseStmt.class);

        UseStmt newNode = new UseStmt(data, Collections.emptyList());

        newNode.set(UseStmt.NAME, moduleName);

        return newNode;
    }

    public OmpReductionClause ompReductionClause(BinaryOperatorKind operator, List<DataRef> refs) {
        DataStore data = newDataStore(OmpReductionClause.class);

        OmpReductionClause newNode= new OmpReductionClause(data, refs);

        newNode.set(OmpReductionClause.KIND, OmpClauseKind.REDUCTION);

        newNode.set(OmpReductionClause.OPERATOR, operator);

        return newNode;
    }
}
