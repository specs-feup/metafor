package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ArrayConstructor extends Expr {
    public ArrayConstructor(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var acSpecification = getAcSpecification();
        return "[" + acSpecification.getCode() + "]";
    }

    private AcSpecification getAcSpecification() {
        return getChild(AcSpecification.class, 0);
    }
}
