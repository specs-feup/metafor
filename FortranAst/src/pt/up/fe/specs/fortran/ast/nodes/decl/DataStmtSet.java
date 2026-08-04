package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DataStmtSet extends FortranNode {
    public DataStmtSet(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataRef> getNames() {
        return getChildrenOf(DataRef.class);
    }

    public List<DataStmtValue> getValues() {
        return getChildrenOf(DataStmtValue.class);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(getNames()
                .stream()
                .map(DataRef::getCode)
                .collect(Collectors.joining(", "))
        );

        code.append("/ ");

        code.append(getValues()
                .stream()
                .map(DataStmtValue::getCode)
                .collect(Collectors.joining(", "))
        );

        code.append(" /");

        return code.toString();
    }
}
