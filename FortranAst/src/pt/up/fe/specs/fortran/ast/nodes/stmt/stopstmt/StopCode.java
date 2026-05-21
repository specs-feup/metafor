package pt.up.fe.specs.fortran.ast.nodes.stmt.stopstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

public class StopCode extends Expr {
    public StopCode(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
