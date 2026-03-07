package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase.*;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.DimensionSpec;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.List;

public class StmtProcessors extends ANodeProcessor {
    public StmtProcessors(FortranJsonResult data) {
        super(data);
    }

    private void executableStmt(ExecutableStmt executableStmt) {
        executableStmt.set(ExecutableStmt.SOURCE, attributes(executableStmt).getString("source"));

        var label = attributes(executableStmt).getString("label");
        if (!label.equals("null")) {
            var labelDecl = factory().labelDecl(Integer.valueOf(label));
            data().processorData().addLabelDecl(labelDecl);
            executableStmt.addChild(0, labelDecl);
        }
    }


    private void actionStmt(ActionStmt actionStmt) {
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
        var variable = getChild(assignmentStmt, FlangName.VARIABLE);
        var expression = getChild(assignmentStmt, FlangName.EXPR);
        assignmentStmt.addChild(variable);
        assignmentStmt.addChild(expression);
    }

    public void stmtBlock(StmtBlock stmtBlock) {
        stmtBlock.setChildren(getChildren(stmtBlock, FlangName.EXECUTION_PART_CONSTRUCT));
    }

    public void ifConstruct(IfConstruct ifConstruct) {
        // Add if-then block
        var ifThenStmt = getChild(ifConstruct, FlangName.IF_THEN_STMT.getStmtAttr());
        var blockStatements = getChildren(ifConstruct, FlangName.EXECUTION_PART_CONSTRUCT);

        var thenBlock = factory().newNode(StmtBlock.class);
        thenBlock.addChildren(blockStatements);

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
        var endIfStmt = getChild(ifConstruct, FlangName.END_IF_STMT.getStmtAttr());
        ifConstruct.addChild(endIfStmt);

        // Assign name if present
        var nameId = attributes(ifThenStmt).getOptionalString(FlangName.NAME.getString());
        if (nameId.isPresent()) {
            var name = attributes().get(nameId.get()).getString("source");
            ifConstruct.setOptional(IfConstruct.NAME, name);
        }
    }

    public void ifThenStmt(IfThenStmt ifThenStmt) {
        var condition = getChild(ifThenStmt, "value");

        ifThenStmt.addChild(0, condition);
    }

    public void elseIfBlock(ElseIfBlock ifElseBlock) {
        var elseIfStmt = getChild(ifElseBlock, FlangName.ELSE_IF_STMT.getStmtAttr());
        ifElseBlock.addChild(elseIfStmt);

        var blockStatements = getChildren(ifElseBlock, FlangName.EXECUTION_PART_CONSTRUCT);
        var block = factory().newNode(StmtBlock.class);
        block.addChildren(blockStatements);
        ifElseBlock.addChild(block);
    }

    public void elseIfStmt(ElseIfStmt elseIfStmt) {
        var condition = getChild(elseIfStmt, "value");
        elseIfStmt.addChild(condition);
    }

    public void elseBlock(ElseBlock elseBlock) {
        var elseStmt = getChild(elseBlock, FlangName.ELSE_STMT.getStmtAttr());
        elseBlock.addChild(elseStmt);

        var blockStatements = getChildren(elseBlock, FlangName.EXECUTION_PART_CONSTRUCT);
        var block = factory().newNode(StmtBlock.class);
        block.addChildren(blockStatements);
        elseBlock.addChild(block);
    }

    public void elseStmt(ElseStmt ignoredElseStmt) {
        // Nothing to do
    }

    public void endIfStmt(EndIfStmt ignoredEndIfStmt) {
        // Nothing to do
    }

    public void ifStmt(IfStmt ifStmt) {
        var condition = getChild(ifStmt, "value");
        var thenStmt = getChild(ifStmt, FlangName.ACTION_STMT.getUnlabeledStmtAttr());

        ifStmt.addChild(condition);
        ifStmt.addChild(thenStmt);
    }

    public void caseConstruct(CaseConstruct caseConstruct) {
        var selectCaseStmt = getChild(caseConstruct, FlangName.SELECT_CASE_STMT.getStmtAttr());
        var caseBlocks = getChildren(caseConstruct, FlangName.CASE);
        var endSelectStmt = getChild(caseConstruct, FlangName.END_SELECT_STMT.getStmtAttr());

        caseConstruct.addChild(selectCaseStmt);
        caseConstruct.addChildren(caseBlocks);
        caseConstruct.addChild(endSelectStmt);
    }

    public void selectCaseStmt(SelectCaseStmt selectCaseStmt) {
        var expr = getChild(selectCaseStmt, "value");

        selectCaseStmt.addChild(expr);
    }

    public void caseBlock(CaseBlock caseBlock) {
        var caseStmt = getChild(caseBlock, FlangName.CASE_STMT.getStmtAttr());
        caseBlock.addChild(caseStmt);

        var blockStatements = getChildren(caseBlock, FlangName.EXECUTION_PART_CONSTRUCT);
        var block = factory().newNode(StmtBlock.class);
        block.addChildren(blockStatements);
        caseBlock.addChild(block);
    }

    public void caseStmt(CaseStmt caseStmt) {
        var caseSelectorId = attributes(caseStmt).getString(FlangName.CASE_SELECTOR);
        var caseSelector = buildCaseSelector(caseSelectorId);

        caseStmt.addChild(caseSelector);
    }

    public CaseSelector buildCaseSelector(String id) {
        var caseSelectorAttrs = attributes().get(id);
        var value = caseSelectorAttrs.getString("value");

        if (value.startsWith("[")) {  // TODO(Process-ing): Improve this
            var caseValueRangeIds = caseSelectorAttrs.getStringList(() -> "value");
            var caseValueRanges = caseValueRangeIds.stream()
                    .map(Object::toString)
                    .map(this::buildCaseValueRange)
                    .toList();

            var caseSelector = factory().newNode(CaseValueRangeList.class);
            caseSelector.addChildren(caseValueRanges);

            return caseSelector;
        }

        return (Default) getNode(value);
    }

    public CaseValueRange buildCaseValueRange(String id) {
        var exprId = attributes().getChildId(id);
        var expr = getNode(exprId);

        var caseValue = factory().newNode(CaseValue.class);
        caseValue.addChild(expr);

        return caseValue;
    }

    public void defaultNode(Default ignoredDefaultNode) {
        // Nothing to do
    }

    public void endSelectStmt(EndSelectStmt ignoredEndSelectStmt) {
        // Nothing to do
    }
}
