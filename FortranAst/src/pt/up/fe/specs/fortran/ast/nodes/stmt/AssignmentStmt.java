package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.variable.Variable;

import java.util.Collection;

public class AssignmentStmt extends ActionStmt {
    public AssignmentStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Variable getVariable() {
        return getChild(Variable.class, 0);
    }

    public Expr getExpression() {
        return getChild(Expr.class, 1);
    }

    @Override
    public String getCode() {
        // a = 1;
        var code = new StringBuilder();

        var variable = getVariable();
        code.append(variable.getCode());

        code.append(" = ");

        var expression = getExpression();
        code.append(expression.getCode());

        return code.toString();
    }
}
