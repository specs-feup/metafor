package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class WhileLoopControl extends ALoopControl {

    public WhileLoopControl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Expr> getCond() {
        return getChildTry(Expr.class);
    }

    @Override
    public String getCode() {
        return getCond().map(
                expr -> keyword(FortranKeyword.WHILE) + " (" + expr.getCode() + ")"
        ).orElse("");
    }
}
