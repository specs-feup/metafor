package pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.GenericSpec;

import java.util.Collection;

public class OnlyGenericSpec extends Only {
    public OnlyGenericSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public GenericSpec getGenericSpec() {
        return getChild(GenericSpec.class, 0);
    }

    @Override
    public String getCode() {
        return getGenericSpec().getCode();
    }
}
