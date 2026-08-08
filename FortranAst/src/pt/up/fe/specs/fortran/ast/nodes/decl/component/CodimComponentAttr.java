package pt.up.fe.specs.fortran.ast.nodes.decl.component;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.coshape.CoarraySpec;

import java.util.Collection;

public class CodimComponentAttr extends ComponentAttr {
    public CodimComponentAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public CoarraySpec getCoarraySpec() {
        return getChild(CoarraySpec.class, 0);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.CODIMENSION) + getCoarraySpec().getCode();
    }
}
