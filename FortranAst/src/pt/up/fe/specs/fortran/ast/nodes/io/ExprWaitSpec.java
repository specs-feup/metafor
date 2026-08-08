package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.ExprConnectSpecKind;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.ExprWaitSpecKind;

import java.util.Collection;

public class ExprWaitSpec extends WaitSpec {
    public static final DataKey<ExprWaitSpecKind> KIND = KeyFactory.enumeration("kind", ExprWaitSpecKind.class);

    public ExprWaitSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ExprWaitSpecKind getKind() {
        return get(KIND);
    }

    public Expr getExpr() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        var kind = getKind();
        var exprCode = getExpr().getCode();

        // We can omit "UNIT=" from the first specifier
        if (kind == ExprWaitSpecKind.UNIT && indexOfSelf() == 0) {
            return exprCode;
        }

        return encase(kind.name()) + "=" + exprCode;
    }
}
