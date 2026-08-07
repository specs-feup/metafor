package pt.up.fe.specs.fortran.ast.nodes.stmt;


import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ArraySpec;

import java.util.Collection;
import java.util.Optional;

public class CommonBlockObject extends FortranNode {
    public static final DataKey<String> NAME = KeyFactory.string("name");

    public CommonBlockObject(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    public Optional<ArraySpec> getArraySpec() {
        return getChildOf(ArraySpec.class);
    }

    @Override
    public String getCode() {
        var name = getName();
        var arraySpecCode = getArraySpec().map(ArraySpec::getCode);

        return name + arraySpecCode.orElse("");
    }
}
