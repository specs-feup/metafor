package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;
import pt.up.fe.specs.fortran.ast.nodes.expr.Literal;

import java.util.Collection;
import java.util.Optional;

public class DataStmtValue extends FortranNode {
    public DataStmtValue(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<IntLiteral> getRepeat() {
        return getNumChildren() == 2
                ? Optional.of(getChild(IntLiteral.class, 0))
                : Optional.empty();
    }

    public Literal getConstant() {
        return getChild(Literal.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var repeatOpt = getRepeat();
        var constant = getConstant();

        var code = new StringBuilder();

        repeatOpt.ifPresent(repeat -> code.append(repeat.getCode()).append("*"));

        code.append(constant.getCode());

        return code.toString();
    }
}
