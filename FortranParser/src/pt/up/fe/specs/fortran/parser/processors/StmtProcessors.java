package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.NamedParameter;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.loops.WhileLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.*;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.EndModuleStmt;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.EndProgramStmt;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.ModuleStmt;
import pt.up.fe.specs.fortran.ast.nodes.program.unit.ProgramStmt;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.specification.LanguageBindingSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.NamedConstantDef;
import pt.up.fe.specs.fortran.ast.nodes.specification.enums.AccessKind;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmtObject;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmtSet;
import pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt.DataStmtVariable;
import pt.up.fe.specs.fortran.ast.nodes.stmt.dimstmt.DimensionDecl;
import pt.up.fe.specs.fortran.ast.nodes.stmt.dimstmt.DimensionStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.enums.ImportKind;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.EndDoStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.*;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.DimensionSpec;
import pt.up.fe.specs.fortran.parser.FlangAttributes;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;
import pt.up.fe.specs.util.SpecsStrings;

import java.util.ArrayList;
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
        if (attrs.has("leadingComments")) {
            var leadingComments = attrs.getStringList("leadingComments");
            stmt.set(Stmt.LEADING_COMMENTS, leadingComments);
        } else {
            stmt.set(Stmt.LEADING_COMMENTS, List.of());
        }

        var trailingComment = attrs.getOptionalString("trailingComment");
        stmt.set(Stmt.TRAILING_COMMENT, trailingComment);

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

        // TODO(Process-ing): Remove this
        var source = attributes(formatStmt).getString("source");
        formatStmt.set(FormatStmt.SOURCE, source);
    }

    public void typeDeclarationStmt(TypeDeclarationStmt typeDeclarationStmt) {
        stmt(typeDeclarationStmt);

        var entityDecls = getChildren(typeDeclarationStmt, FlangName.ENTITY_DECL);

        var type = getChild(typeDeclarationStmt, FlangName.DECLARATION_TYPE_SPEC);
        typeDeclarationStmt.addChild(type);

        // TODO(Process-ing): See why this is needed
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
        assignmentStmt.addChild(variable);

        var expression = getChild(assignmentStmt, FlangName.EXPR);
        assignmentStmt.addChild(expression);
    }

    public void stmtBlock(StmtBlock stmtBlock) {
        stmtBlock.setChildren(getChildren(stmtBlock, FlangName.EXECUTION_PART_CONSTRUCT));
    }

    public void ifConstruct(IfConstruct ifConstruct) {
        // Add if-then block
        var ifThenStmt = getStmtChild(ifConstruct, FlangName.IF_THEN_STMT);

        var thenBlock = factory().newNode(Execution.class);
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
        var block = factory().newNode(Execution.class);
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
        var block = factory().newNode(Execution.class);
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

    public void doConstruct(DoConstruct doConstruct) {
        var name = attributes().getOptionalString(doConstruct, "source", FlangName.NON_LABEL_DO_STMT, FlangName.NAME);
        name.ifPresent(str -> doConstruct.setOptional(DoConstruct.NAME, str));

        var doStmt = getStmtChild(doConstruct, FlangName.NON_LABEL_DO_STMT);
        doConstruct.addChild(doStmt);

        var doStmtSource = attributes(doStmt).getString("source");
        var doLabel = extractLabel(doStmtSource);
        doConstruct.set(DoConstruct.DO_LABEL, doLabel);

        var bodyStmts = new ArrayList<>(getChildren(doConstruct, FlangName.EXECUTION_PART_CONSTRUCT));

        // In labeled do loops with no continue statement at the end, Flang will add
        // an empty continue statement at the end, which we want to ignore
        var lastStmt = bodyStmts.get(bodyStmts.size() - 1);
        if (attributes(lastStmt).getOptionalString("source").map(String::isEmpty).orElse(false)) {
            bodyStmts.remove(bodyStmts.size() - 1);  // Remove last element
        }

        var body = factory().newNode(Execution.class, bodyStmts);
        doConstruct.addChild(body);

        var endDoStmt = getStmtChild(doConstruct, FlangName.END_DO_STMT);
        doConstruct.addChild(endDoStmt);
    }

    private Optional<Integer> extractLabel(String doStmtSource) {
        // Ignore spaces, for compatibility with fixed-form (where the provided source lacks spacing)
        doStmtSource = doStmtSource.replace(" ", "");

        // Ignore 'do' keyword
        doStmtSource = doStmtSource.substring(2);
        var labelText = new StringBuilder();
        for (var i = 0; i < doStmtSource.length(); i++) {
            var c = doStmtSource.charAt(i);
            if (!Character.isDigit(c)) {
                break;
            }

            labelText.append(c);
        }

        return SpecsStrings.tryGetDecimalInteger(labelText.toString());
    }

    public void doStmt(DoStmt doStmt) {
        stmt(doStmt);

        var control = attributes(doStmt).getOptionalString(FlangName.LOOP_CONTROL);
        control.ifPresentOrElse(
                s -> {
                    var attrs = attributes().getAttrs(s);

                    var childKey = FlangName.convertTry(attrs.getVariantKey()).orElseThrow();
                    var value = attrs.getString(childKey);

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
                    var empty = factory().newNode(WhileLoopControl.class);
                    doStmt.addChild(empty);
                }
        );
    }

    public void endDoStmt(EndDoStmt endDoStmt) {
        stmt(endDoStmt);
    }

    public void compilerDirective(CompilerDirective compilerDirective) {
        stmt(compilerDirective);

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

    public void containsStmt(ContainsStmt containsStmt) {
        executableStmt(containsStmt);
    }

    public void allocateStmt(AllocateStmt allocateStmt) {
        actionStmt(allocateStmt);

        var allocations = getChildren(allocateStmt, FlangName.ALLOCATION);
        allocateStmt.addChildren(allocations);

        if (attributes(allocateStmt).has(FlangName.ALLOC_OPT)) {
            var options = getChildren(allocateStmt, FlangName.ALLOC_OPT);
            allocateStmt.addChildren(options);
        }
    }

    public void deallocateStmt(DeallocateStmt deallocateStmt) {
        actionStmt(deallocateStmt);

        deallocateStmt.addChildren(getChildren(deallocateStmt, FlangName.ALLOCATE_OBJECT));
    }

    public void useStmt(UseStmt useStmt) {
        stmt(useStmt);

        var intrinsic = attributes(useStmt).getOptionalString("nature")
                .map(nature -> !nature.endsWith("Non_Intrinsic"));
        useStmt.set(UseStmt.INTRINSIC, intrinsic);

        var nameId = attributes(useStmt).getString("moduleName");
        var name = attributes().get(nameId).getString("source");
        useStmt.set(UseStmt.NAME, name);
    }

    public void useRenameStmt(UseRenameStmt useRenameStmt) {
        useStmt(useRenameStmt);

        var renameList = getChildren(useRenameStmt, FlangName.RENAME);
        useRenameStmt.addChildren(renameList);
    }

    public void useOnlyStmt(UseOnlyStmt useOnlyStmt) {
        useStmt(useOnlyStmt);

        var onlyList = getChildren(useOnlyStmt, FlangName.ONLY);
        useOnlyStmt.addChildren(onlyList);
    }

    public void useName(UseName useName) {
        var name = attributes().getString(useName, "source", FlangName.NAME);
        useName.set(UseName.NAME, name);
    }

    public void namesRename(NamesRename namesRename) {
        var localNameId = attributes().getString(namesRename, "local");
        var localName = attributes().get(localNameId).getString("source");
        namesRename.set(NamesRename.LOCAL_NAME, localName);

        var globalNameId = attributes().getString(namesRename, "global");
        var globalName = attributes().get(globalNameId).getString("source");
        namesRename.set(NamesRename.GLOBAL_NAME, globalName);
    }

    public void continueStmt(ContinueStmt continueStmt) {
        actionStmt(continueStmt);
    }

    public void parameterStmt(ParameterStmt parameterStmt) {
        stmt(parameterStmt);

        parameterStmt.addChildren(getChildren(parameterStmt, FlangName.NAMED_CONSTANT_DEF));
    }

    public void externalStmt(ExternalStmt externalStmt) {
        externalStmt.addChildren(getChildren(externalStmt, FlangName.NAME));
    }

    public void namedConstantDef(NamedConstantDef namedConstantDef) {
        var ref = getChild(namedConstantDef, FlangName.NAMED_CONSTANT);
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

    public void programStmt(ProgramStmt programStmt) {
        stmt(programStmt);

        var name = attributes().getString(programStmt, "source", FlangName.NAME);
        programStmt.set(ProgramStmt.PROGRAM_NAME, name);
    }

    public void endProgramStmt(EndProgramStmt endProgramStmt) {
        stmt(endProgramStmt);
    }

    public void subroutineStmt(SubroutineStmt subroutineStmt) {
        stmt(subroutineStmt);

        var name = attributes().getString(subroutineStmt, "source", FlangName.NAME);
        subroutineStmt.set(SubroutineStmt.SUBROUTINE_NAME, name);

        if (attributes(subroutineStmt).has(FlangName.DUMMY_ARG)) {
            var dummyArgs = getChildren(subroutineStmt, FlangName.DUMMY_ARG);
            subroutineStmt.addChildren(dummyArgs);
        }
    }

    public void endSubroutineStmt(EndSubroutineStmt endSubroutineStmt) {
        stmt(endSubroutineStmt);
    }

    public void returnStmt(ReturnStmt returnStmt) {
        executableStmt(returnStmt);

        var target = attributes()
                .getOptionalString(returnStmt, "CharBlock", FlangName.EXPR, FlangName.LITERAL_CONSTANT, FlangName.INT_LITERAL_CONSTANT)
                .map(Integer::parseInt);
        returnStmt.set(ReturnStmt.TARGET, target);
    }

    public void dimensionStmt(DimensionStmt dimensionStmt) {
        stmt(dimensionStmt);

        var decls = getChildren(dimensionStmt, FlangName.DECLARATION);
        dimensionStmt.addChildren(decls);
    }

    public void dimensionDecl(DimensionDecl dimensionDecl) {
        var name = attributes().getString(dimensionDecl, "source", FlangName.NAME);
        dimensionDecl.set(DimensionDecl.NAME, name);

        var arraySpec = getChild(dimensionDecl, FlangName.ARRAY_SPEC);
        dimensionDecl.addChild(arraySpec);
    }

    public void namelistStmt(NamelistStmt namelistStmt) {
        stmt(namelistStmt);

        var groups = getChildren(namelistStmt, FlangName.GROUP);
        namelistStmt.addChildren(groups);
    }

    public void namelistGroup(NamelistGroup namelistGroup) {
        var attrs = attributes(namelistGroup);

        var groupNameId = attrs.getString("groupName");
        var groupName = attributes().get(groupNameId).getString("source");
        namelistGroup.set(NamelistGroup.GROUP_NAME, groupName);

        var objectNames = attrs.getStringList("objectNames").stream()
                .map(objectNameId -> attributes().get(objectNameId).getString("source"))
                .toList();
        namelistGroup.set(NamelistGroup.OBJECT_NAMES, objectNames);
    }

    public void functionStmt(FunctionStmt functionStmt) {
        stmt(functionStmt);

        var functionNameId = attributes(functionStmt).getString("functionName");
        var functionName = attributes().get(functionNameId).getString("source");
        functionStmt.set(FunctionStmt.FUNCTION_NAME, functionName);

        var parameterNames = attributes(functionStmt).getStringList("paramNames");
        var parameters = parameterNames.stream()
                .map(this::toNamedParameter)
                .toList();
        functionStmt.addChildren(parameters);

        var suffixId = attributes(functionStmt).getOptionalString("suffix");
        suffixId.map(id -> attributes().get(id))
                .ifPresent(suffixAttrs -> {
                    var bindingId = suffixAttrs.getOptionalString("binding");
                    bindingId.ifPresent(id -> {
                        var binding = getChild(id);
                        functionStmt.addChild(binding);
                    });

                    var resultName = suffixAttrs.getOptionalString("resultName")
                            .map(nameId -> attributes().get(nameId).getString("source"));
                    functionStmt.set(FunctionStmt.RESULT_NAME, resultName);
                });
    }

    private NamedParameter toNamedParameter(String nameId) {
        var name = attributes().get(nameId).getString("source");
        return factory().namedParameter(name);
    }

    public void languageBindingSpec(LanguageBindingSpec languageBindingSpec) {
        var name = getChild(languageBindingSpec, FlangName.EXPR);
        languageBindingSpec.addChild(name);

        var cDefined = attributes(languageBindingSpec).getString("bool");
        languageBindingSpec.set(LanguageBindingSpec.C_DEFINED, cDefined.equals("1"));
    }

    public void endFunctionStmt(EndFunctionStmt endFunctionStmt) {
        stmt(endFunctionStmt);
    }

    public void moduleStmt(ModuleStmt moduleStmt) {
        stmt(moduleStmt);

        var moduleName = attributes().getString(moduleStmt, "source", FlangName.NAME);
        moduleStmt.set(ModuleStmt.MODULE_NAME, moduleName);
    }

    public void endModuleStmt(EndModuleStmt endModuleStmt) {
        stmt(endModuleStmt);
    }

    public void accessStmt(AccessStmt accessStmt) {
        stmt(accessStmt);

        var accessKindSrc = attributes().getString(accessStmt, "value", FlangName.ACCESS_SPEC, FlangName.KIND);
        var accessKind = AccessKind.valueOf(accessKindSrc.toUpperCase());
        accessStmt.set(AccessStmt.ACCESS_KIND, accessKind);

        if (attributes(accessStmt).has(FlangName.ACCESS_ID)) {
            var accessIds = getChildren(accessStmt, FlangName.ACCESS_ID);
            accessStmt.addChildren(accessIds);
        }
    }

    public void importStmt(ImportStmt importStmt) {
        stmt(importStmt);

        var kindSrc = attributes().getString(importStmt, "value", FlangName.IMPORT_KIND);
        var kind = ImportKind.valueOf(kindSrc.toUpperCase());
        importStmt.set(ImportStmt.KIND, kind);

        if (attributes(importStmt).has(FlangName.NAME)) {
            var nameIds = attributes(importStmt).getStringList(FlangName.NAME);
            var names = nameIds.stream()
                    .map(nameId -> attributes().get(nameId).getString("source"))
                    .toList();
            importStmt.set(ImportStmt.NAMES, names);
        }
    }

    public void defaultImplicitStmt(DefaultImplicitStmt implicitStmt) {
        stmt(implicitStmt);

        var specs = getChildren(implicitStmt, FlangName.IMPLICIT_SPEC);
        implicitStmt.addChildren(specs);
    }

    public void implicitNoneStmt(ImplicitNoneStmt implicitNoneStmt) {
        var attrs = attributes(implicitNoneStmt);
        var specs = attrs.getStringList(FlangName.IMPLICIT_NONE_NAME_SPEC);
        var specValues = specs.stream()
                .map(id -> attributes().get(id).getString("value"))
                .toList();

        implicitNoneStmt.set(ImplicitNoneStmt.EXPLICIT_TYPES, specValues.contains("Type"));
        implicitNoneStmt.set(ImplicitNoneStmt.EXPLICIT_EXTERNAL, specValues.contains("External"));
    }
}
