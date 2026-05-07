package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class StopStmt extends ActionStmt {
    public StopStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return FortranKeyword.STOP.getKeyword(false);
    }
}
