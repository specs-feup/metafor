package pt.up.fe.specs.fortran.ast.nodes.expr.args;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public abstract class ArgSpec extends FortranNode {
    public static final DataKey<Optional<String>> KEYWORD = KeyFactory.optional("keyword");

    public ArgSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getKeyword() {
        return get(KEYWORD);
    }

    protected String getKeywordCode() {
        return getKeyword().map(keyword -> keyword + " = ").orElse("");
    }
}
