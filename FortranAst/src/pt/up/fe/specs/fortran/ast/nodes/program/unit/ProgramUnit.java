package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * R502 program-unit
 */
public abstract class ProgramUnit extends FortranNode {
    public ProgramUnit(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
