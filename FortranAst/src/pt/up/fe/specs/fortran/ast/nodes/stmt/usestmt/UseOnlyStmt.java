package pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UseOnlyStmt extends UseStmt {
    public UseOnlyStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<Only> getOnlySpecs() {
        return getChildrenOf(Only.class);
    }

    @Override
    public String getStmtCode() {
        var usePrefix = getUseStmtPrefix();

        var onlyListSuffix = getOnlySpecs().stream()
                .map(Only::getCode)
                .collect(Collectors.joining(", "));

        return usePrefix + ", " + keyword(FortranKeyword.ONLY) + " : " + onlyListSuffix;
    }
}
