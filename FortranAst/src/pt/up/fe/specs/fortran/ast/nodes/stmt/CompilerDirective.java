package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.utils.NameValue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CompilerDirective extends Stmt {
    public CompilerDirective(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<NameValue> getPairs() {
        return getChildrenOf(NameValue.class);
    }

    @Override
    public String getStmtCode() {
        return "!DIR$ " + getPairs().stream()
                .map(NameValue::getCode)
                .collect(Collectors.joining(" "));
    }

    public String getDirectiveString() {
        return getPairs().stream()
                .map(NameValue::getCode)
                .collect(Collectors.joining(" "));
    }
}
