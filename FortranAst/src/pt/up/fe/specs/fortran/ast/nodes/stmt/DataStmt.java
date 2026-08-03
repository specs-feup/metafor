package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.DataStmtSet;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DataStmt extends DeclarationStmt {
    public DataStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataStmtSet> getSets() {
        return getChildrenOf(DataStmtSet.class);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(FortranKeyword.DATA.getKeyword(false)).append(" ");

        code.append(getSets()
                .stream()
                .map(DataStmtSet::getCode)
                .collect(Collectors.joining(", "))
        );

        return code.toString();
    }
}
