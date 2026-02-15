package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.enums.DoKind;

import java.util.Collection;
import java.util.Optional;

public class DoStmt extends Stmt {

    public DoStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<LoopControl> getControl() {
        return getChildTry(LoopControl.class);
    }

    public DoKind getKind() {
        return getControl()
                .map(LoopControl::getKind)
                .orElse(DoKind.WHILE);
    }
}
