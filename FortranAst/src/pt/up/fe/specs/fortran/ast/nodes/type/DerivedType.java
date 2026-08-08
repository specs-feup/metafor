package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.TypeParam;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DerivedType extends FortranType {
    public static final DataKey<String> NAME = KeyFactory.string("name");

    public DerivedType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    public List<TypeParam> getTypeParams() {
        return getChildren(TypeParam.class);
    }

    @Override
    public String getCode() {
        var typeParams = getTypeParams();

        var typeParamsCode = typeParams.isEmpty()
                ? ""
                : typeParams.stream()
                  .map(TypeParam::getCode)
                  .collect(Collectors.joining(", ", " (", ")"));

        return getName() + typeParamsCode;
    }
}
