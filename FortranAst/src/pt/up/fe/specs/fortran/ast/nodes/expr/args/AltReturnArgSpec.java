package pt.up.fe.specs.fortran.ast.nodes.expr.args;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class AltReturnArgSpec extends ArgSpec {
    public static final DataKey<Integer> LABEL = KeyFactory.integer("label");

    public AltReturnArgSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public int getLabel() {
        return get(LABEL);
    }

    @Override
    public String getCode() {
        var keywordCode = getKeywordCode();
        var labelCode = Integer.toString(getLabel());

        return keywordCode + "*" + labelCode;
    }
}
