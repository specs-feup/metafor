package pt.up.fe.specs.fortran.ast.nodes.stmt.componentdef;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class DataComponentDefStmt extends ComponentDefStmt {
    public DataComponentDefStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    
}
