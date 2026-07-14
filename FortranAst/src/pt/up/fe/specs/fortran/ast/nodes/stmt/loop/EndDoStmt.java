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

        var doLabelOpt = parent.getDoLabel();
        var labelPrefix = doLabelOpt.map(label -> label + " ").orElse("");

        var doNameOpt = parent.getName();
        var nameSuffix = doNameOpt.map(name -> " " + name).orElse("");

        return labelPrefix + keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.DO) + nameSuffix;
    }
}
