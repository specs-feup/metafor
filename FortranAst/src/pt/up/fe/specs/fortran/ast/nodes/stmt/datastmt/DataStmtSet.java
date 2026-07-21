package pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.DataStmtValue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DataStmtSet extends FortranNode {
    public DataStmtSet(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataStmtObject> getObjects() {
        return getChildrenOf(DataStmtObject.class);
    }

    public List<DataStmtValue> getValues() {
        return getChildrenOf(DataStmtValue.class);
    }

    @Override
    public String getCode() {
        var objects = getObjects();
        var values = getValues();

        var objectsCode = objects.stream()
                .map(DataStmtObject::getCode)
                .collect(Collectors.joining("," + optSpc()));
        var valuesCode = values.stream()
                .map(DataStmtValue::getCode)
                .collect(Collectors.joining("," + optSpc()));

        return objectsCode + optSpc() + "/" + optSpc() + valuesCode + optSpc() + "/";
    }
}
