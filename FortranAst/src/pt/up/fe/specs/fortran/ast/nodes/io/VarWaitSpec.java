package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.VarWaitSpecKind;

import java.util.Collection;

public class VarWaitSpec extends WaitSpec {
    public static final DataKey<VarWaitSpecKind> KIND = KeyFactory.enumeration("kind", VarWaitSpecKind.class);

    public VarWaitSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public VarWaitSpecKind getKind() {
        return get(KIND);
    }

    public Variable getVariable() {
        return getChild(Variable.class, 0);
    }

    @Override
    public String getCode() {
        return encase(getKind().name()) + "=" + getVariable().getCode();
    }
}
