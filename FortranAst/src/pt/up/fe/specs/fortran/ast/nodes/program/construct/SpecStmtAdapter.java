package pt.up.fe.specs.fortran.ast.nodes.program.construct;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.SpecStmt;

import java.util.Collection;

public class SpecStmtAdapter extends SpecConstruct {
    public SpecStmtAdapter(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public SpecStmt getSpecificationStmt() {
        return getChild(SpecStmt.class, 0);
    }

    @Override
    public String getCode() {
        return getSpecificationStmt().getCode();
    }
}
