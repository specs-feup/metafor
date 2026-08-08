package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.VarCloseSpecKind;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.VarConnectSpecKind;

import java.util.Collection;

public class VarCloseSpec extends CloseSpec {
    public static final DataKey<VarCloseSpecKind> KIND = KeyFactory.enumeration("kind", VarCloseSpecKind.class);

    public VarCloseSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public VarCloseSpecKind getKind() {
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
