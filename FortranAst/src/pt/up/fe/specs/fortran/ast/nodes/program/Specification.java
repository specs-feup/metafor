package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Specification extends StmtBlock {

    public Specification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
