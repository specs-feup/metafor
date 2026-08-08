package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

/**
 * R1138 end-if-stmt
 */
public class EndIfStmt extends Stmt {
    public EndIfStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        var nameOpt = ((IfConstruct) getParent()).getName();

        var code = new StringBuilder();

        code.append(keyword(FortranKeyword.END)).append(" ").append(keyword(FortranKeyword.IF));

        if (!fixedForm()) {
            nameOpt.ifPresent(name -> code.append(" ").append(name));
        }

        return code.toString();
    }
}
