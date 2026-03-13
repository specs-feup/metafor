package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ConcurrentLoopControl extends LoopControl {
    public ConcurrentLoopControl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<ConcurrentRange> getRanges() {
        return getChildrenOf(ConcurrentRange.class);
    }

    public Optional<Expr> getMask() {
        return getChildTry(Expr.class);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(FortranKeyword.CONCURRENT).append(" (");

        code.append(String.join(
                ", ",
                getRanges().stream().map(ConcurrentRange::getCode).toList()
        ));

        getMask().ifPresent(mask -> code.append(", ").append(mask.getCode()));

        code.append(")");

        return code.toString();
    }
}
