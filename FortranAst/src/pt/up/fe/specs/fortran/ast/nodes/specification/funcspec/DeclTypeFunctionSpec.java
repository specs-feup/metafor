package pt.up.fe.specs.fortran.ast.nodes.specification.funcspec;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DeclType;

import java.util.Collection;

public class DeclTypeFunctionSpec extends FunctionSpec {
    public DeclTypeFunctionSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DeclType getDeclType() {
        return getChild(DeclType.class, 0);
    }

    @Override
    public String getCode() {
        return getDeclType().getCode();
    }
}
