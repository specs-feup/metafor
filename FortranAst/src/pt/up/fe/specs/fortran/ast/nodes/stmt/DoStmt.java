package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopBounds;
import pt.up.fe.specs.fortran.ast.nodes.loops.enums.DoKind;

import java.util.Collection;
import java.util.Optional;

public class DoStmt extends Stmt {

    public final static DataKey<DoKind> KIND = KeyFactory.enumeration("op", DoKind.class);

    public DoStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<LoopBounds> getBounds() {
        return getChildTry(LoopBounds.class);
    }

    public Optional<Expr> getCond() {
        return getChildTry(Expr.class);
    }

    public DoKind getKind() {
        return get(KIND);
    }
}
