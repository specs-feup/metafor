package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.utils.Format;
import pt.up.fe.specs.fortran.ast.nodes.utils.IoUnit;

import java.util.Collection;
import java.util.Optional;

public class WriteStmt extends ActionStmt {
    public WriteStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<IoUnit> getIoUnit() {
        return getChildTry(IoUnit.class);
    }

    public Optional<Format> getFormat() {
        return getChildTry(Format.class);
    }

    @Override
    public String getCode() {
        return FortranKeyword.WRITE + "(" + getIoUnit().get().getCode() + ", " + getFormat().get().getCode() + ")";
    }
}
