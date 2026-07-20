package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;


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
}
