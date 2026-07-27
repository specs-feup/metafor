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
        var formatCode = getFormat().getCode();

        // We can omit "FMT=" on a format specifier right after the first unit specifier (if non-labeled too)
        if (indexOfSelf() == 1) {
            return formatCode;
        }

        return keyword(FortranKeyword.FMT) + "=" + formatCode;
    }
}
