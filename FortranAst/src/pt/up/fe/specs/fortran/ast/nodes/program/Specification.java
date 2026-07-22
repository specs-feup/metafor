package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.SpecificationStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.UseStmt;

import java.util.Collection;
import java.util.List;

public class Specification extends StmtBlock {

    public Specification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<SpecificationStmt> getSpecificationStatements() {
        return getChildrenOf(SpecificationStmt.class);
    }

    public void addUseStmt(UseStmt stmt) {
        addChild(0, stmt);
    }
}
