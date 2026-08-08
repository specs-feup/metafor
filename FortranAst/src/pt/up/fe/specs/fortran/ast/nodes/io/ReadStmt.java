package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ActionStmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReadStmt extends ActionStmt {
    public ReadStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<IoControlSpec> getSpecs() {
        return getChildrenOf(IoControlSpec.class);
    }

    public List<InputItem> getItems() {
        return getChildrenOf(InputItem.class);
    }

    @Override
    public String getStmtCode() {
        var specsCode = getSpecs().stream()
                .map(IoControlSpec::getCode)
                .collect(Collectors.joining(", ", "(", ")"));

        var itemsCode = getItems().stream()
                .map(InputItem::getCode)
                .collect(Collectors.joining(", "));

        return keyword(FortranKeyword.READ) + specsCode + " " + itemsCode;
    }
}
