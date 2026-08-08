package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.VarPosFlushSpecKind;

import java.util.Collection;

public class VarPosFlushSpec extends PosFlushSpec {
    public static final DataKey<VarPosFlushSpecKind> KIND = KeyFactory.enumeration("kind", VarPosFlushSpecKind.class);

    public VarPosFlushSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public VarPosFlushSpecKind getKind() {
        return get(KIND);
    }

    public Variable getVariable() {
        return getChild(Variable.class, 0);
    }

    @Override
    public String getCode() {
        var kindCode = encase(getKind().name());
        var varCode = getVariable().getCode();

        return kindCode + "=" + varCode;
    }
}
