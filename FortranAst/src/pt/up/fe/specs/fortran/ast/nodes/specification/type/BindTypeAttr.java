package pt.up.fe.specs.fortran.ast.nodes.specification.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class BindTypeAttr extends TypeAttr {
    public BindTypeAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.BIND) + "(" + keyword(FortranKeyword.C) + ")";
    }
}
