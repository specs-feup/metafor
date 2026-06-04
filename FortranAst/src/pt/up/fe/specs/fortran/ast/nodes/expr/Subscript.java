package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Subscript extends SectionSubscript {
    public Subscript(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getValue() {
        return getChild(Expr.class);
    }
}
