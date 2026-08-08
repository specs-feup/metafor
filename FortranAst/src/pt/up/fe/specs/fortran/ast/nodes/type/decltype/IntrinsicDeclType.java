package pt.up.fe.specs.fortran.ast.nodes.type.decltype;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.IntrinsicType;

import java.util.Collection;

public class IntrinsicDeclType extends DeclType {
    public IntrinsicDeclType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IntrinsicType getIntrinsicType() {
        return getChild(IntrinsicType.class, 0);
    }

    @Override
    public String getCode() {
        return getIntrinsicType().getCode();
    }
}
