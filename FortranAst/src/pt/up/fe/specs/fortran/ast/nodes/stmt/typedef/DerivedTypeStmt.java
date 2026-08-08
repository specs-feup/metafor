package pt.up.fe.specs.fortran.ast.nodes.stmt.typedef;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.NamedParameter;
import pt.up.fe.specs.fortran.ast.nodes.specification.type.TypeAttr;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.security.Key;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DerivedTypeStmt extends Stmt {
    public static final DataKey<String> TYPE_NAME = KeyFactory.string("type_name");

    public DerivedTypeStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<TypeAttr> getTypeAttrs() {
        return getChildrenOf(TypeAttr.class);
    }

    public String getTypeName() {
        return get(TYPE_NAME);
    }

    public List<NamedParameter> getParameters() {
        return getChildrenOf(NamedParameter.class);
    }

    @Override
    public String getStmtCode() {
        var typeAttrsCode = getTypeAttrs().stream()
                .map(attr -> ", " + attr.getCode())
                .collect(Collectors.joining());

        var params = getParameters();
        var paramsCode = params.isEmpty() ? "" : params.stream()
                .map(NamedParameter::getCode)
                .collect(Collectors.joining(", ", "(", ")"));

        return keyword(FortranKeyword.TYPE) + typeAttrsCode + " :: " + getTypeName() + paramsCode;
    }
}
