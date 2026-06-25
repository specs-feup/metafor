package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommonBlock extends FortranNode {
    public static final DataKey<Optional<String>> NAME = KeyFactory.optional("name");

    public CommonBlock(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getName() {
        return get(NAME);
    }

    public List<CommonBlockObject> getObjects() {
        return getChildren(CommonBlockObject.class);
    }

    @Override
    public String getCode() {
        var name = getName();
        var nameCode = name.map(n -> "/" + n + "/").orElse("");

        var objects = getObjects();
        var objectsCode = objects.stream()
                .map(CommonBlockObject::getCode)
                .collect(Collectors.joining(", "));

        return nameCode + " " + objectsCode;
    }
}
