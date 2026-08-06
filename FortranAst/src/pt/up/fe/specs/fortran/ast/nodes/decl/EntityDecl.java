package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DeclType;

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


    public DeclType getType() {
        return getChild(DeclType.class, 0);
    }

    public Optional<Initialization> getInitialization() {
        return getChildOf(Initialization.class);
    }

    public Optional<ArraySpecification> getArraySpec() {
        return getChildOf(ArraySpecification.class);
    }


    @Override
    public String getCode() {
        var code = new StringBuilder();

        code.append(get(NAME));

        getArraySpec().ifPresent(arraySpec -> code.append(arraySpec.getCode()));

        getInitialization().ifPresent(init -> code.append(init.getCode()));

        return code.toString();
    }
}
