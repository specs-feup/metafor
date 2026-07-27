package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.ExprIoControlSpecKind;

import java.util.Collection;

public class StarUnitIoControlSpec extends IoControlSpec {
    public StarUnitIoControlSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        // We can omit "UNIT=" from the first specifier
        return indexOfSelf() == 0 ? "*" : keyword(FortranKeyword.UNIT) + "=*";
    }
}
