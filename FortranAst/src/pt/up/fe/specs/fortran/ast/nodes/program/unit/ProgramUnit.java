package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.InternalPart;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * R502 program-unit
 */
public abstract class ProgramUnit extends FortranNode {
    public ProgramUnit(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
