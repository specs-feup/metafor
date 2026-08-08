package pt.up.fe.specs.fortran.ast.nodes.alloc;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.enums.VarAllocOptionKind;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;

import java.util.Collection;

public class VarAllocOption extends AllocOption {
    public static final DataKey<VarAllocOptionKind> KIND = KeyFactory.enumeration("kind", VarAllocOptionKind.class);

    public VarAllocOption(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public VarAllocOptionKind getKind() {
        return get(KIND);
    }

    public Variable getVariable() {
        return getChild(Variable.class);
    }

    @Override
    public String getCode() {
        return encase(getKind().name()) + "=" + getVariable().getCode();
    }
}
