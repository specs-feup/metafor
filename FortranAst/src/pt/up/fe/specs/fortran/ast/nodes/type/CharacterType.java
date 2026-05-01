package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class CharacterType extends IntrinsicType {
    public CharacterType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<CharSelector> getSelector() {
        return getChildTry(CharSelector.class);
    }

    @Override
    public String getCode() {
        return "character" + getSelector().map(CharSelector::getCode).orElse("");
    }
}
