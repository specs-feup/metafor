package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.loops.WhileLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.specification.NamedConstantDef;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmtObject;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmtSet;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmtVariable;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.*;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.DimensionSpec;
import pt.up.fe.specs.fortran.parser.FlangAttributes;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.List;
import java.util.Optional;

public class StmtProcessors extends ANodeProcessor {
    public StmtProcessors(FortranJsonResult data) {
        super(data);
    }

    public void stmt(Stmt stmt) {
        stmtWithAttrs(stmt, attributes(stmt));
    }

    public void stmtWithAttrs(Stmt stmt, FlangAttributes attrs) {
        if (attrs.has("comments")) {
            var comments = attrs.getStringList("comments");
            stmt.set(Stmt.COMMENTS, comments);
        } else {
            stmt.set(Stmt.COMMENTS, List.of());
        }

        var labelOpt = attrs.getOptionalString("label");
        labelOpt.ifPresent(label -> {
            var labelDecl = factory().labelDecl(Integer.parseInt(label));
            data().processorData().addLabelDecl(labelDecl);
            stmt.addChild(0, labelDecl);
        });
    }

    public void executableStmt(ExecutableStmt executableStmt) {
        stmt(executableStmt);

        // Uncomment this if we really need the statement source
        // executableStmt.set(ExecutableStmt.SOURCE, attributes(executableStmt).getString("source"));
    }


    public void actionStmt(ActionStmt actionStmt) {
        executableStmt(actionStmt);
    }


    public void printStmt(PrintStmt printStmt) {
        actionStmt(printStmt);

        printStmt.addChild(getChild(printStmt, FlangName.FORMAT));
        printStmt.addChildren(getChildren(printStmt, FlangName.OUTPUT_ITEM));
    }

    public void formatStmt(FormatStmt formatStmt) {
        executableStmt(formatStmt);
    }

    public void typeDeclarationStmt(TypeDeclarationStmt typeDeclarationStmt) {
        stmt(typeDeclarationStmt);

        var entityDecls = getChildren(typeDeclarationStmt, FlangName.ENTITY_DECL);

        var type = getChild(typeDeclarationStmt, FlangName.DECLARATION_TYPE_SPEC);

        entityDecls.stream().forEach(entityDecl -> entityDecl.addChild(0, type));

        typeDeclarationStmt.addChildren(entityDecls);

        if (attributes(typeDeclarationStmt).has(FlangName.ATTR_SPEC)) {
            var attributes = getChildren(typeDeclarationStmt, FlangName.ATTR_SPEC);
            var processedAttributes = attributes.stream()
                    .map(attr -> attr instanceof ArraySpecification
                            ? factory().newNode(DimensionSpec.class, List.of(attr))
                            : attr)
                    .toList();
            typeDeclarationStmt.addChildren(processedAttributes);
        }
    }

    public void assignmentStmt(AssignmentStmt assignmentStmt) {
        actionStmt(assignmentStmt);

        var variable = getChild(assignmentStmt, FlangName.VARIABLE);
        var expression = getChild(assignmentStmt, FlangName.EXPR);
        assignmentStmt.addChild(variable);
        assignmentStmt.addChild(expression);
    }

    public void stmtBlock(StmtBlock stmtBlock) {
        stmtBlock.setChildren(getChildren(stmtBlock, FlangName.EXECUTION_PART_CONSTRUCT));
    }

    public void ifConstruct(IfConstruct ifConstruct) {
        executableStmt(ifConstruct);

        // Add if-then block
        var ifThenStmt = getStmtChild(ifConstruct, FlangName.IF_THEN_STMT);

        var thenBlock = factory().newNode(StmtBlock.class);
        if (attributes(ifConstruct).has(FlangName.EXECUTION_PART_CONSTRUCT)) {
            var blockStatements = getChildren(ifConstruct, FlangName.EXECUTION_PART_CONSTRUCT);
            thenBlock.addChildren(blockStatements);
        }

        var ifThenBlock = factory().newNode(IfThenBlock.class);
        ifThenBlock.addChild(ifThenStmt);
        ifThenBlock.addChild(thenBlock);

        ifConstruct.addChild(ifThenBlock);

        // Add else-if blocks
        if (attributes(ifConstruct).has(FlangName.ELSE_IF_BLOCK)) {
            var elseIfBlocks = getChildren(ifConstruct, FlangName.ELSE_IF_BLOCK);
            elseIfBlocks.forEach(ifConstruct::addChild);
        }

        // Add else block
        if (attributes(ifConstruct).has(FlangName.ELSE_BLOCK)) {
            var elseBlock = getChild(ifConstruct, FlangName.ELSE_BLOCK);
            ifConstruct.addChild(elseBlock);
        }

        // Add end if statement
        var endIfStmt = getStmtChild(ifConstruct, FlangName.END_IF_STMT);
        ifConstruct.addChild(endIfStmt);

        // Assign name if present
        var nameId = attributes(ifThenStmt).getOptionalString(FlangName.NAME.getString());
        if (nameId.isPresent()) {
            var name = attributes().get(nameId.get()).getString("source");
            ifConstruct.setOptional(IfConstruct.NAME, name);
        }
    }

    public void ifThenStmt(IfThenStmt ifThenStmt) {
        actionStmt(ifThenStmt);

        var condition = getChild(ifThenStmt, FlangName.EXPR);

        ifThenStmt.addChild(0, condition);
    }

    public void elseIfBlock(ElseIfBlock ifElseBlock) {
        var elseIfStmt = getStmtChild(ifElseBlock, FlangName.ELSE_IF_STMT);
        ifElseBlock.addChild(elseIfStmt);

        var blockStatements = getChildren(ifElseBlock, FlangName.EXECUTION_PART_CONSTRUCT);
        var block = factory().newNode(StmtBlock.class);
        block.addChildren(blockStatements);
        ifElseBlock.addChild(block);
    }

    public void elseIfStmt(ElseIfStmt elseIfStmt) {
        stmt(elseIfStmt);

        var condition = getChild(elseIfStmt, FlangName.EXPR);
        elseIfStmt.addChild(condition);
    }

    public void elseBlock(ElseBlock elseBlock) {
        var elseStmt = getStmtChild(elseBlock, FlangName.ELSE_STMT);
        elseBlock.addChild(elseStmt);

        var blockStatements = getChildren(elseBlock, FlangName.EXECUTION_PART_CONSTRUCT);
        var block = factory().newNode(StmtBlock.class);
        block.addChildren(blockStatements);
        elseBlock.addChild(block);
    }

    public void elseStmt(ElseStmt elseStmt) {
        stmt(elseStmt);
    }

    public void endIfStmt(EndIfStmt endIfStmt) {
        stmt(endIfStmt);
    }

    public void ifStmt(IfStmt ifStmt) {
        actionStmt(ifStmt);

        var condition = getChild(ifStmt, FlangName.EXPR);
        var thenStmt = getUnlabeledStmtChild(ifStmt, FlangName.ACTION_STMT);

        ifStmt.addChild(condition);
        ifStmt.addChild(thenStmt);
    }

    public void caseConstruct(CaseConstruct caseConstruct) {
        executableStmt(caseConstruct);

        var selectCaseStmt = getStmtChild(caseConstruct, FlangName.SELECT_CASE_STMT);
        var caseBlocks = getChildren(caseConstruct, FlangName.CASE);
        var endSelectStmt = getStmtChild(caseConstruct, FlangName.END_SELECT_STMT);

        caseConstruct.addChild(selectCaseStmt);
        caseConstruct.addChildren(caseBlocks);
        caseConstruct.addChild(endSelectStmt);

        // Assign name if present
        var nameId = attributes(selectCaseStmt).getOptionalString(FlangName.NAME.getString());
        if (nameId.isPresent()) {
            var name = attributes().get(nameId.get()).getString("source");
            caseConstruct.setOptional(CaseConstruct.NAME, name);
        }
    }

    public void selectCaseStmt(SelectCaseStmt selectCaseStmt) {
        stmt(selectCaseStmt);

        var expr = getChild(selectCaseStmt, FlangName.EXPR);

        selectCaseStmt.addChild(expr);
    }

    public void caseBlock(CaseBlock caseBlock) {
        var caseStmtWrapperId = attributes(caseBlock).getString(FlangName.CASE_STMT.getStmtAttr());
        var caseStmtId = attributes().get(caseStmtWrapperId).getString("statement");
        var caseStmt = buildCaseStmt(caseStmtId);

        var blockStatements = getChildren(caseBlock, FlangName.EXECUTION_PART_CONSTRUCT);
        var block = factory().newNode(StmtBlock.class);
        block.addChildren(blockStatements);

        caseBlock.addChild(caseStmt);
        caseBlock.addChild(block);
    }

    public CaseStmt buildCaseStmt(String id) {
        var caseStmtAttrs = attributes().get(id);

        var caseSelectorId = caseStmtAttrs.getString(FlangName.CASE_SELECTOR);
        var caseSelectorAttrs = attributes().get(caseSelectorId);

        // If they exist, extract the list of case value ranges
        if (caseSelectorAttrs.has(FlangName.CASE_VALUE_RANGE)) {
            var caseValueRangeIds = caseSelectorAttrs.getStringList(FlangName.CASE_VALUE_RANGE);
            var caseValueRanges = caseValueRangeIds.stream()
                    .map(Object::toString)
                    .map(this::buildCaseValueRange)
                    .toList();

            var valueCaseStmt = factory().newNode(ValueCaseStmt.class);
            stmtWithAttrs(valueCaseStmt, caseSelectorAttrs);
            valueCaseStmt.addChildren(caseValueRanges);

            return valueCaseStmt;
        }

        // If selector has a default child, build a default case statement
        if (caseSelectorAttrs.has(FlangName.DEFAULT)) {
            var defaultCaseStmt = factory().newNode(DefaultCaseStmt.class);
            stmtWithAttrs(defaultCaseStmt, caseSelectorAttrs);
            return defaultCaseStmt;
        }

        throw new RuntimeException("Unknown case selector: " + caseSelectorId);
    }

    public CaseValueRange buildCaseValueRange(String id) {
        var attrs = attributes().get(id);

        // If child is an instance of Range, build an appropriate range
        if (attrs.has(FlangName.RANGE)) {
            var rangeId = attrs.getString(FlangName.RANGE);
            var rangeAttrs = attributes().get(rangeId);

            Optional<FortranNode> lowerBound = rangeAttrs.getOptionalString("lower").map(this::getChild);
            Optional<FortranNode> upperBound = rangeAttrs.getOptionalString("upper").map(this::getChild);

            if (lowerBound.isPresent()) {
                if (upperBound.isPresent()) {
                    var fullRange = factory().newNode(CaseFullRange.class);
                    fullRange.addChild(lowerBound.get());
                    fullRange.addChild(upperBound.get());

                    return fullRange;

                } else {
                    var lowerBoundRange = factory().newNode(CaseLowerRange.class);
                    lowerBoundRange.addChild(lowerBound.get());

                    return lowerBoundRange;
                }

            } else if (upperBound.isPresent()) {
                var upperBoundRange = factory().newNode(CaseUpperRange.class);
                upperBoundRange.addChild(upperBound.get());

                return upperBoundRange;

            } else {
                throw new RuntimeException("Range must have at least a lower or an upper bound");
            }
        }

        // If child is an instance of Expr, build a single case value
        if (attrs.has(FlangName.EXPR)) {
            var childId = attrs.getString(FlangName.EXPR);
            var exprId = attributes().getChildId(childId);
            var expr = getNode(exprId);

            var caseValue = factory().newNode(CaseValue.class);
            caseValue.addChild(expr);

            return caseValue;
        }

        throw new RuntimeException("Could not determine case value range type for id: " + id + " with attributes: " + attrs);
    }

    public void endSelectStmt(EndSelectStmt endSelectStmt) {
        stmt(endSelectStmt);
    }

    public void doStmt(DoStmt doStmt) {
        executableStmt(doStmt);

        Optional<String> control = attributes().getOptionalString(doStmt, "id", FlangName.NON_LABEL_DO_STMT, FlangName.LOOP_CONTROL);
        Optional<String> name = attributes().getOptionalString(doStmt, "source", FlangName.NON_LABEL_DO_STMT, FlangName.NAME);

        name.ifPresent(str -> doStmt.setOptional(DoStmt.NAME, str));

        control.ifPresentOrElse(
                s -> {
                    FlangAttributes attrs = attributes().getAttrs(s);

                    FlangName childKey = FlangName.convertTry(attrs.getVariantKey()).orElseThrow();
                    String value = attrs.getString(childKey);

                    switch (childKey) {
                        case LOOP_BOUNDS, CONCURRENT -> {
                            doStmt.addChild(getChild(value));
                        }
                        case EXPR -> {
                            Expr cond = (Expr) getChild(value);
                            WhileLoopControl middleman = factory().newNode(WhileLoopControl.class);

                            middleman.addChild(cond);
                            doStmt.addChild(middleman);
                        }
                        default -> throw new RuntimeException("Unknown loop control type: " + childKey);
                    }
                },
                () -> {
                    WhileLoopControl empty = factory().newNode(WhileLoopControl.class);
                    doStmt.addChild(empty);
                }
        );

        Execution body = factory().newNode(Execution.class, getChildren(doStmt, FlangName.EXECUTION_PART_CONSTRUCT));
        doStmt.addChild(body);
    }

    public void compilerDirective(CompilerDirective compilerDirective) {
        executableStmt(compilerDirective);

        var variantKey = attributes(compilerDirective).getVariantKey();
        compilerDirective.addChildren(getChildren(compilerDirective, variantKey));
    }

    public void callStmt(CallStmt callStmt) {
        actionStmt(callStmt);

        callStmt.addChild(getChild(callStmt, "call"));
    }

    public void gotoStmt(GotoStmt gotoStmt) {
        actionStmt(gotoStmt);

        var strLabel = attributes(gotoStmt).getString("uint64_t");
        var label = Integer.parseInt(strLabel);

        gotoStmt.set(GotoStmt.LABEL, label);
    }

    public void writeStmt(WriteStmt writeStmt) {
        actionStmt(writeStmt);

        if (attributes(writeStmt).has("iounit")) {
            writeStmt.addChild(getChild(writeStmt, "iounit"));
        }

        if (attributes(writeStmt).has("format")) {
            writeStmt.addChild(getChild(writeStmt, "format"));
        }

        if (attributes(writeStmt).has("controls")) {
            writeStmt.addChildren(getChildren(writeStmt, "controls"));
        }

        if (attributes(writeStmt).has("items")) {
            writeStmt.addChildren(getChildren(writeStmt, "items"));
        }
    }

    public void containsStmt(ContainsStmt containsStmt) {
        executableStmt(containsStmt);
    }

    public void allocateStmt(AllocateStmt allocateStmt) {
        actionStmt(allocateStmt);

        allocateStmt.addChildren(getChildren(allocateStmt, FlangName.ALLOCATION));
        allocateStmt.addChildren(getChildren(allocateStmt, FlangName.ALLOC_OPT));
    }

    public void deallocateStmt(DeallocateStmt deallocateStmt) {
        actionStmt(deallocateStmt);

        deallocateStmt.addChildren(getChildren(deallocateStmt, FlangName.ALLOCATE_OBJECT));
    }

    public void useStmt(UseStmt useStmt) {
        stmt(useStmt);

        String nameId = attributes(useStmt).getString("moduleName");
        String name = attributes().get(nameId).getString("source");

        useStmt.set(UseStmt.NAME, name);
    }

    public void continueStmt(ContinueStmt continueStmt) {
        actionStmt(continueStmt);
    }

    public void parameterStmt(ParameterStmt parameterStmt) {
        stmt(parameterStmt);

        parameterStmt.addChildren(getChildren(parameterStmt, FlangName.NAMED_CONSTANT_DEF));
    }

    public void namedConstantDef(NamedConstantDef namedConstantDef) {
        var ref = getChild(namedConstantDef, FlangName.NAMED_CONSTANT_DEF);
        namedConstantDef.addChild(ref);

        var expr = getChild(namedConstantDef, FlangName.EXPR);
        namedConstantDef.addChild(expr);
    }

    public void stopStmt(StopStmt stopStmt) {
        actionStmt(stopStmt);

        var kindId = attributes(stopStmt).getString("kind");
        var kind = attributes().get(kindId).getString("value");
        stopStmt.set(StopStmt.ERROR_STOP, kind.equals("ErrorStop"));

        var stopCodeOpt = getChildOptional(stopStmt, "code");
        stopCodeOpt.ifPresent(stopCode -> {
            stopStmt.addChild(stopCode);
            stopStmt.set(StopStmt.HAS_CODE, true);
        });

        var quietOpt = getChildOptional(stopStmt, "quiet");
        quietOpt.ifPresent(stopStmt::addChild);
    }

    public void dataStmt(DataStmt dataStmt) {
        stmt(dataStmt);

        var dataStmtSets = getChildren(dataStmt, FlangName.DATA_STMT_SET);
        dataStmt.addChildren(dataStmtSets);
    }

    public void dataStmtSet(DataStmtSet dataStmtSet) {
        var dataStmtObjectIds = attributes(dataStmtSet).getStringList(FlangName.DATA_STMT_OBJECT);
        var dataStmtObjects = dataStmtObjectIds.stream()
                .map(this::createDataStmtObject)
                .toList();
        dataStmtObjects.forEach(this::dataStmtObject);
        dataStmtSet.addChildren(dataStmtObjects);

        var dataStmtValues = getChildren(dataStmtSet, FlangName.DATA_STMT_VALUE);
        dataStmtSet.addChildren(dataStmtValues);
    }

    public DataStmtObject createDataStmtObject(String id) {
        var attrs = attributes().get(id);

        if (attrs.has(FlangName.VARIABLE)) {
            return factory().newNode(DataStmtVariable.class, List.of(), id);
        }

        throw new RuntimeException("Unrecognizable DataStmtObject: " + attrs);
    }

    public void dataStmtObject(DataStmtObject object) {
        if (object instanceof DataStmtVariable variable) {
            dataStmtVariable(variable);
        }
    }

    public void dataStmtVariable(DataStmtVariable dataStmtVariable) {
        var variable = getChild(dataStmtVariable, FlangName.VARIABLE);
        dataStmtVariable.addChild(variable);
    }

    public void commonStmt(CommonStmt commonStmt) {
        stmt(commonStmt);

        var blocks = getChildren(commonStmt, FlangName.COMMON_STMT_BLOCK);
        commonStmt.addChildren(blocks);
    }

    public void commonBlock(CommonBlock commonBlock) {
        var name = attributes().getOptionalString(commonBlock, "source", FlangName.NAME);
        commonBlock.set(CommonBlock.NAME, name);

        var objects = getChildren(commonBlock, FlangName.COMMON_BLOCK_OBJECT);
        commonBlock.addChildren(objects);
    }

    public void commonBlockObject(CommonBlockObject object) {
        var name = attributes().getString(object, "source", FlangName.NAME);
        object.set(CommonBlockObject.NAME, name);

        var arraySpecOpt = getChildOptional(object, FlangName.ARRAY_SPEC);
        arraySpecOpt.ifPresent(object::addChild);
    }
}
