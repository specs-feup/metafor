package pt.up.fe.specs.fortran.ast.nodes.stmt.loop;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndDoStmt extends Stmt {
    public EndDoStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        var parent = getAncestor(DoConstruct.class);
        if (parent.hasContinueEnd()) {
            return "";
        }

        var doNameOpt = parent.getName();
        var doNameSuffix = doNameOpt.map(name -> " " + name).orElse("");

        return keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.DO) + doNameSuffix;
    }
}
