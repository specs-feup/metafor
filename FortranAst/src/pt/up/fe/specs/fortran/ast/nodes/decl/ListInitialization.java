package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ListInitialization extends Initialization {
    public ListInitialization(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataStmtValue> getDataStmtValueList() {
        return getChildrenOf(DataStmtValue.class);
    }

    @Override
    public String getCode() {
        var dataStmtValues = getDataStmtValueList();

        return dataStmtValues.stream()
                .map(FortranNode::getCode)
                .collect(Collectors.joining(", ", "/ ", "/"));
    }
}
