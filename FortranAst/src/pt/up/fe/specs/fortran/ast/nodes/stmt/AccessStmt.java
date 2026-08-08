package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.enums.AccessKind;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.GenericSpec;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AccessStmt extends SpecStmt {
    public static final DataKey<AccessKind> ACCESS_KIND = KeyFactory.enumeration("access_kind", AccessKind.class);

    public AccessStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public AccessKind getAccessKind() {
        return get(ACCESS_KIND);
    }

    public List<GenericSpec> getIds() {
        return getChildren(GenericSpec.class);
    }

    @Override
    public String getStmtCode() {
        var accessKindCode = encase(getAccessKind().name());

        var ids = getIds();
        if (ids.isEmpty()) {
            return accessKindCode;
        }

        var idsCode = ids.stream()
                .map(GenericSpec::getCode)
                .collect(Collectors.joining(", "));

        return accessKindCode + " :: " + idsCode;
    }
}
