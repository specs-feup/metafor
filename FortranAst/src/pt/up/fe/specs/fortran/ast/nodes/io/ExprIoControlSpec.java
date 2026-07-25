package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.ExprIoControlSpecKind;

import java.util.Collection;

public class ExprIoControlSpec extends IoControlSpec {
    public static DataKey<ExprIoControlSpecKind> KIND = KeyFactory.enumeration("kind", ExprIoControlSpecKind.class);

    public ExprIoControlSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ExprIoControlSpecKind getKind() {
        return get(KIND);
    }

    public Expr getExpr() {
        return getChild(Expr.class);
    }

    @Override
    public String getCode() {
        var kindCode = encase(getKind().name());
        var exprCode = getExpr().getCode();

        return kindCode + "=" + exprCode;
    }
}
