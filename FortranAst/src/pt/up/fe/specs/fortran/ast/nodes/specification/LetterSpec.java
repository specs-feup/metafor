package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class LetterSpec extends FortranNode {
    public static final DataKey<Character> FIRST_LETTER = KeyFactory.object("first_letter", Character.class);
    public static final DataKey<Optional<Character>> LAST_LETTER = KeyFactory.optional("last_letter");

    public LetterSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public char getFirstLetter() {
        return get(FIRST_LETTER);
    }

    public Optional<Character> getLastLetter() {
        return get(LAST_LETTER);
    }

    @Override
    public String getCode() {
        var firstLetterCode = Character.toString(getFirstLetter());
        var lastLetterCode = getLastLetter()
                .map(letter -> "-" + letter)
                .orElse("");

        return firstLetterCode + lastLetterCode;
    }
}
