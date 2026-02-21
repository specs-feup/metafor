package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.type.FortranType;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.ArraySpecifier;

import java.util.Collection;
import java.util.Optional;

public class EntityDecl extends FortranDecl {

    // DATAKEYS BEGIN

    /**
     * The name of the entity.
     */
    public final static DataKey<String> NAME = KeyFactory.string("name");


    // DATAKEYS END

    public EntityDecl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }


    public FortranType getType() {
        return getChild(FortranType.class, 0);
    }

    public Optional<Expr> getInitialization() {
        if (getNumChildren() < 2) {
            return Optional.empty();
        }

        return getChildOf(Expr.class);
    }

    public Optional<ArraySpecifier> getArraySpec() {
        if (getNumChildren() < 2) {
            return Optional.empty();
        }

        return getChildOf(ArraySpecifier.class);
    }


    @Override
    public String getCode() {
        var code = new StringBuilder();

        code.append(get(NAME));

        getArraySpec().ifPresent(arraySpec -> code.append(arraySpec.getCode()));

        getInitialization().ifPresent(init -> code.append(" = ").append(init.getCode()));

        return code.toString();
    }
}
