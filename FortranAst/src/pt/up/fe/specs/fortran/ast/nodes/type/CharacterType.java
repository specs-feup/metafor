package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.LenSelector;

import java.util.Collection;
import java.util.Optional;

public class CharacterType extends IntrinsicType {
    public CharacterType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<LenSelector> getSelector() {
        return getChildTry(LenSelector.class);
    }

    @Override
    public String getCode() {
        var selectorCode = getSelector().map(LenSelector::getCode).orElse("");

        return keyword(FortranKeyword.CHARACTER) + selectorCode;
    }
}
