package pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UseRenameStmt extends UseStmt {
    public UseRenameStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<Rename> getRenames() {
        return getChildrenOf(Rename.class);
    }

    @Override
    public String getStmtCode() {
        var usePrefix = getUseStmtPrefix();

        var renamesSuffix = getRenames().stream()
                .map(rename -> ", " + rename.getCode())
                .collect(Collectors.joining());

        return usePrefix + renamesSuffix;
    }
}
