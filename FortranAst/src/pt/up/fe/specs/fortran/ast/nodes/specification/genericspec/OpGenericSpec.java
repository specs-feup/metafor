package pt.up.fe.specs.fortran.ast.nodes.specification.genericspec;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.DefinedOperator;

import java.util.Collection;

public class OpGenericSpec extends GenericSpec {
    public OpGenericSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DefinedOperator getOperator() {
        return getChild(DefinedOperator.class, 0);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.OPERATOR) + "(" + getOperator().getCode() + ")";
    }
}
