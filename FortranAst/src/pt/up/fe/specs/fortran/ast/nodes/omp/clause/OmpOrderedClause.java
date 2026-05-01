package pt.up.fe.specs.fortran.ast.nodes.omp.clause;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;

import java.util.Collection;

public class OmpOrderedClause extends OmpClause {

    public OmpOrderedClause(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IntLiteral getValue() {
        return getChild(IntLiteral.class);
    }

    @Override
    public String getCode() {
        return get(KIND).getCode() + "(" + getValue().getCode() + ")";
    }
}
