package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.List;


/**
 * R509 execution-part
 * <p>
 * executable-construct [execution-part-construct]...
 * <p>
 * Contains statements to execute
 */
public class Execution extends StmtBlock {

    public Execution(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<ExecutableStmt> getExecutableStatements() {
        return getChildren(ExecutableStmt.class);
    }


    @Override
    public String getCode() {
        var code = new StringBuilder();
        for (var stmt : getExecutableStatements()) {

            //System.out.println("LABEL: " + stmt.getLabel());
            stmt.getLabel().ifPresent(label -> code.append(label.get(LabelDecl.LABEL) + " "));

            code.append(stmt.getCode()).append(ln());
        }
        return code.toString();
    }
}
