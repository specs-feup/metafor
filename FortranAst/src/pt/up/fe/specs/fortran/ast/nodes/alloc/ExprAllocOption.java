package pt.up.fe.specs.fortran.ast.nodes.alloc;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.enums.ExprAllocOptionKind;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

public class ExprAllocOption extends AllocOption {
    public static final DataKey<ExprAllocOptionKind> KIND = KeyFactory.enumeration("kind", ExprAllocOptionKind.class);

    public ExprAllocOption(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ExprAllocOptionKind getKind() {
        return get(KIND);
    }

    public Expr getExpr() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        return encase(getKind().name()) + "=" + getExpr().getCode();
    }
}
