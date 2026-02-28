package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.loops.WhileLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.enums.DoKind;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.Optional;

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

    public void doStmt(DoStmt doStmt) {
        Optional<String> control = attributes().getOptionalString(doStmt, "id", FlangName.NON_LABEL_DO_STMT, FlangName.LOOP_CONTROL);

        control.ifPresentOrElse(
                s -> {
                    DoKind kind = DoKind.valueOf(attributes().getAttrs(s).getString("kind"));
                    String value = attributes().getAttrs(s).getString("value");

                    switch (kind) {
                        case Range, Concurrent -> {
                            doStmt.addChild(getChild(value));
                        }
                        case While -> {
                            Expr cond = (Expr) getChild(value);
                            WhileLoopControl middleman = factory().newNode(WhileLoopControl.class);

                            middleman.addChild(cond);
                            doStmt.addChild(middleman);
                        }
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
}
