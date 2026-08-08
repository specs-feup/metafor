package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.LabelIoControlSpecKind;

import java.util.Collection;

public class LabelIoControlSpec extends IoControlSpec {
    public static final DataKey<Integer> LABEL = KeyFactory.integer("label");
    public static final DataKey<LabelIoControlSpecKind> KIND = KeyFactory.enumeration("kind", LabelIoControlSpecKind.class);

    public LabelIoControlSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public LabelIoControlSpecKind getKind() {
        return get(KIND);
    }

    public Integer getLabel() {
        return get(LABEL);
    }

    @Override
    public String getCode() {
        var kindCode = encase(getKind().name());
        var labelCode = getLabel().toString();

        return kindCode + "=" + labelCode;
    }
}
