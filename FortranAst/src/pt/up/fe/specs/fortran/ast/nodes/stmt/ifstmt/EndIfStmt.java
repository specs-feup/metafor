package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class EndIfStmt extends FortranNode {
    public EndIfStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var nameOpt = ((IfConstruct) getParent()).getName();

        var code = new StringBuilder();

        code.append(FortranKeyword.END)
                .append(" ")
                .append(FortranKeyword.IF);

        nameOpt.ifPresent(name -> code.append(" ").append(name));

        return code.toString();
    }
}
