package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import static pt.up.fe.specs.util.SpecsStrings.toCamelCase;

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

        typeDeclarationStmt.setChildren(entityDecls);
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
        var ifThenStmt = getChild(ifConstruct, "Statement<" + toCamelCase(FlangName.IF_THEN_STMT.name()) + ">");
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
        var endIfStmt = getChild(ifConstruct, "Statement<" + toCamelCase(FlangName.END_IF_STMT.name()) + ">");
        ifConstruct.addChild(endIfStmt);

        // Assign name if present
        var nameId = attributes(ifThenStmt).getOptionalString(toCamelCase(FlangName.NAME.name()));
        if (nameId.isPresent()) {
            var name = attributes().get(nameId.get()).getString("source");
            ifConstruct.setOptional(IfConstruct.NAME, name);
        }
    }

    public void ifThenStmt(IfThenStmt ifThenStmt) {
        var condition = getChild(ifThenStmt, FlangName.SCALAR);

        ifThenStmt.addChild(0, condition);
    }

    public void elseIfBlock(ElseIfBlock ifElseBlock) {
        var elseIfStmt = getChild(ifElseBlock, "Statement<" + toCamelCase(FlangName.ELSE_IF_STMT.name()) + ">");
        ifElseBlock.addChild(elseIfStmt);

        var blockStatements = getChildren(ifElseBlock, FlangName.EXECUTION_PART_CONSTRUCT);
        var block = factory().newNode(StmtBlock.class);
        block.addChildren(blockStatements);
        ifElseBlock.addChild(block);
    }

    public void elseIfStmt(ElseIfStmt elseIfStmt) {
        var condition = getChild(elseIfStmt, FlangName.SCALAR);
        elseIfStmt.addChild(condition);
    }

    public void elseBlock(ElseBlock elseBlock) {
        var elseStmt = getChild(elseBlock, "Statement<" + toCamelCase(FlangName.ELSE_STMT.name()) + ">");
        elseBlock.addChild(elseStmt);

        var blockStatements = getChildren(elseBlock, FlangName.EXECUTION_PART_CONSTRUCT);
        var block = factory().newNode(StmtBlock.class);
        block.addChildren(blockStatements);
        elseBlock.addChild(block);
    }

    public void elseStmt(ElseStmt elseStmt) {
        // Nothing to do
    }

    public void endIfStmt(EndIfStmt endIfStmt) {
        // Nothing to do
    }
}
