package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.ExprConnectSpecKind;

import java.util.Collection;

public class ExprConnectSpec extends ConnectSpec {
    public static final DataKey<ExprConnectSpecKind> KIND = KeyFactory.enumeration("kind", ExprConnectSpecKind.class);

    public ExprConnectSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ExprConnectSpecKind getKind() {
        return get(KIND);
    }

    public Expr getExpr() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        var kindCode = encase(getKind().name());
        var exprCode = getExpr().getCode();

        return kindCode + "=" + exprCode;
    }
}
