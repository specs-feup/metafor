package pt.up.fe.specs.fortran.ast.nodes.io.ctrlspec;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.utils.enums.IoControlSpecKind;

import java.util.Collection;

public class IoControlSpec extends FortranNode {
    public static DataKey<IoControlSpecKind> KIND = KeyFactory.enumeration("kind", IoControlSpecKind.class);

    public IoControlSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IoControlSpecKind getKind() {
        return get(KIND);
    }

    public Expr getValue() {
        return getChild(Expr.class);
    }

    @Override
    public String getCode() {
        return getKind().getString() + '=' + getValue().getCode();
    }
}
