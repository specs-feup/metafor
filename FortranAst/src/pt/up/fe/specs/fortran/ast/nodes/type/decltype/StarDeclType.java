package pt.up.fe.specs.fortran.ast.nodes.type.decltype;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.enums.DeclTypeKind;

import java.util.Collection;

public class StarDeclType extends DeclType {
    public static final DataKey<DeclTypeKind> KIND = KeyFactory.enumeration("kind", DeclTypeKind.class);

    public StarDeclType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DeclTypeKind getKind() {
        return get(KIND);
    }

    @Override
    public String getCode() {
        var kindCode = encase(getKind().name());

        return kindCode + "(*)";
    }
}
