package pt.up.fe.specs.fortran.ast.nodes.program.construct;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.CompilerDirective;

import java.util.Collection;

public class SpecDirectiveAdapter extends SpecConstruct {
    public SpecDirectiveAdapter(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public CompilerDirective getCompilerDirective() {
        return getChild(CompilerDirective.class, 0);
    }

    @Override
    public String getCode() {
        return getCompilerDirective().getCode();
    }
}
