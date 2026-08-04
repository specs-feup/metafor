package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ArraySubscriptExpr extends DataRef {

    public ArraySubscriptExpr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getRef() {
        return getChild(DataRef.class, 0);
    }

    public List<SectionSubscript> getSubscripts() {
        return getChildren(SectionSubscript.class, 1);
    }

    @Override
    public String getCode() {
        return getRef().getCode() + "(" +
                getSubscripts().stream()
                        .map(SectionSubscript::getCode)
                        .collect(Collectors.joining(", ")) +
                ")";
    }
}
