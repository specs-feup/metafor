package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class LegacyKindSelector extends KindSelector {
    public LegacyKindSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var kindOpt = getKind();
        return kindOpt.map(kind -> "*" + kind).orElse("");
    }
}
