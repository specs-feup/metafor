package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class NamelistIoControlSpec extends IoControlSpec {
    public static final DataKey<String> NAMELIST_NAME = KeyFactory.string("namelistName");

    public NamelistIoControlSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getNamelistName() {
        return get(NAMELIST_NAME);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.NML) + "=" + getNamelistName();
    }
}
