package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;

import java.util.Collection;

public class IntrinsicOperator extends DefinedOperator {
    public static final DataKey<BinaryOperatorKind> OPERATOR_KIND = KeyFactory.enumeration("kind", BinaryOperatorKind.class);

    public IntrinsicOperator(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public BinaryOperatorKind getOperatorKind() {
        return get(OPERATOR_KIND);
    }

    @Override
    public String getCode() {
        return getOperatorKind().getString();
    }
}
