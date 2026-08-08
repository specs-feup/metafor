package pt.up.fe.specs.fortran.ast.nodes.specification.shape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class AssumedRankSpec extends ArraySpec {
    public AssumedRankSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return "..";
    }
}
