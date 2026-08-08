package pt.up.fe.specs.fortran.ast.nodes.specification.type;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ExtendsTypeAttr extends TypeAttr {
    public static final DataKey<String> PARENT_TYPE = KeyFactory.string("parent_type");

    public ExtendsTypeAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getParentType() {
        return get(PARENT_TYPE);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.EXTENDS) + "(" + getParentType() + ")";
    }
}
