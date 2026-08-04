package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.KindSelector;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents Fortran integer types.
 */
public class LogicalType extends IntrinsicType {
    public LogicalType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<KindSelector> getKindSelector() {
        return getChildOf(KindSelector.class);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.LOGICAL) + getKindSelector().map(FortranNode::getCode).orElse("");
    }
}
