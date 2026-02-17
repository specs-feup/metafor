package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.List;

public class IfThenStmt extends FortranNode {
    public IfThenStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getCondition() {
        return getChild(Expr.class, 0);
    }

    public List<ExecutableStmt> getExecutableStmts() {
        return getChildren(ExecutableStmt.class, 1);
    }

    @Override
    public String getCode() {
        var code = new StringBuilder();

        var condition = getCondition();
        var executableStmts = getExecutableStmts();

        code.append(keyword(FortranKeyword.IF))
                .append(" (")
                .append(condition.getCode())
                .append(") ")
                .append(FortranKeyword.THEN)
                .append(ln());

        for (var stmt : executableStmts) {
            code.append(stmt.getCode()).append(ln());
        }

        return code.toString();
    }
}
