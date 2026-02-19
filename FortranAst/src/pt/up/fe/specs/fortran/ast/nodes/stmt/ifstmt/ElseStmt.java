package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ElseStmt extends FortranNode {
    public ElseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getCode() {
        return keyword(FortranKeyword.ELSE);
    }
}
