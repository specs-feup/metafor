package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.KindSelector;

import java.util.Collection;
import java.util.Optional;

public class ComplexType extends IntrinsicType {
    public ComplexType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<KindSelector> getKindSelector() {
        return getChildOf(KindSelector.class);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.COMPLEX) + getKindSelector().map(FortranNode::getCode).orElse("");
    }
}
