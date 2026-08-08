package pt.up.fe.specs.fortran.ast.nodes.specification.genericspec;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.enums.GenericSpecKind;

import java.util.Collection;

public class OtherGenericSpec extends GenericSpec {
    public static final DataKey<GenericSpecKind> KIND = KeyFactory.enumeration("kind", GenericSpecKind.class);

    public OtherGenericSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public GenericSpecKind getKind() {
        return get(KIND);
    }

    @Override
    public String getCode() {
        return switch (getKind()) {
            case ASSIGNMENT -> keyword(FortranKeyword.ASSIGNMENT) + "(=)";
            case READ_FORMATTED -> keyword(FortranKeyword.READ) + "(" + keyword(FortranKeyword.FORMATTED) + ")";
            case READ_UNFORMATTED -> keyword(FortranKeyword.READ) + "(" + keyword(FortranKeyword.UNFORMATTED) + ")";
            case WRITE_FORMATTED -> keyword(FortranKeyword.WRITE) + "(" + keyword(FortranKeyword.FORMATTED) + ")";
            case WRITE_UNFORMATTED -> keyword(FortranKeyword.WRITE) + "(" + keyword(FortranKeyword.UNFORMATTED) + ")";
        };
    }
}
