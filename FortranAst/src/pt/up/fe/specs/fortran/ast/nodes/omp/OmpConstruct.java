package pt.up.fe.specs.fortran.ast.nodes.omp;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;

import java.util.Collection;
import java.util.List;

abstract public class OmpConstruct extends FortranNode {

    public final static DataKey<List<OmpDirectiveKind>> KINDS = KeyFactory.enumerationMulti("kinds", OmpDirectiveKind.class);

    public OmpConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
