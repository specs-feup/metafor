package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * R1137 else-stmt
 */
public class ElseStmt extends FortranNode {
    public ElseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getCode() {
        var nameOpt = ((IfConstruct) getParent().getParent()).getName();

        var code = new StringBuilder();

        code.append(keyword(FortranKeyword.ELSE));

        nameOpt.ifPresent(name -> code.append(" ").append(name));

        return code.toString();
    }
}
