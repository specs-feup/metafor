package pt.up.fe.specs.fortran.ast.nodes.program.construct;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.DeclStmt;

import java.util.Collection;

public class DeclStmtAdapter extends DeclConstruct {
    public DeclStmtAdapter(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DeclStmt getDeclarationStmt() {
        return getChild(DeclStmt.class, 0);
    }

    @Override
    public String getCode() {
        return getDeclarationStmt().getCode();
    }
}
