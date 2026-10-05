package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class ComponentSpec extends FortranNode {
    public static final DataKey<Optional<String>> KEYWORD = KeyFactory.optional("keyword");

    public ComponentSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getKeyword() {
        return get(KEYWORD);
    }

    public Expr getDataSource() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        var keywordCode = getKeyword().map(keyword -> keyword + " = ").orElse("");
        var dataSourceCode = getDataSource().getCode();

        return keywordCode + dataSourceCode;
    }
}
