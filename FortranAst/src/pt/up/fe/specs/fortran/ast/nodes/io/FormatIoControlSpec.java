package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.utils.Format;

import java.util.Collection;

public class FormatIoControlSpec extends IoControlSpec {
    public FormatIoControlSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Format getFormat() {
        return getChild(Format.class, 0);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.FMT) + "=" + getFormat().getCode();
    }
}
