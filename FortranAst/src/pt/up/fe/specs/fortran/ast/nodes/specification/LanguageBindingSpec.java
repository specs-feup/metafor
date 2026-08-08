package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class LanguageBindingSpec extends FortranNode {
    public static final DataKey<Boolean> C_DEFINED = KeyFactory.bool("c_defined");

    public LanguageBindingSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Expr> getName() {
        return getChildTry(Expr.class, 0);
    }

    public boolean isCDefined() {
        return get(C_DEFINED);
    }

    @Override
    public String getCode() {
        var nameCode = getName()
                .map(name -> ", " + keyword(FortranKeyword.NAME) + "=" + name.getCode())
                .orElse("");
        var cDefinedCode = isCDefined() ? ", " + keyword(FortranKeyword.CDEFINED) : "";

        return keyword(FortranKeyword.BIND) + "("
                + keyword(FortranKeyword.C) + nameCode + cDefinedCode + ")";
    }
}
