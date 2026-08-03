package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.Subprogram;

import java.util.Collection;

public class SubprogramUnit extends ProgramUnit {
    public SubprogramUnit(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Subprogram getSubprogram() {
        return getChild(Subprogram.class, 0);
    }

    @Override
    public String getCode() {
        return getSubprogram().getCode();
    }
}
