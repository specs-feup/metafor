package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.ExprIoControlSpecKind;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.VarIoControlSpecKind;

import java.util.Collection;

public class VarIoControlSpec extends IoControlSpec {
    public static final DataKey<VarIoControlSpecKind> KIND = KeyFactory.enumeration("kind", VarIoControlSpecKind.class);

    public VarIoControlSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public VarIoControlSpecKind getKind() {
        return get(KIND);
    }

    public Variable getVariable() {
        return getChild(Variable.class, 0);
    }

    @Override
    public String getCode() {
        var kind = getKind();
        var variableCode = getVariable().getCode();

        // We can omit "UNIT=" from the first specifier
        if (kind == VarIoControlSpecKind.UNIT && indexOfSelf() == 0) {
            return variableCode;
        }

        return encase(kind.name()) + "=" + variableCode;
    }
}
