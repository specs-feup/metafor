package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImplicitNoneStmt extends ImplicitStmt {
    public static final DataKey<Boolean> EXPLICIT_TYPES = KeyFactory.bool("explicit_types");
    public static final DataKey<Boolean> EXPLICIT_EXTERNAL = KeyFactory.bool("explicit_external");

    public ImplicitNoneStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public boolean explicitTypes() {
        return get(EXPLICIT_TYPES);
    }

    public boolean explicitExternal() {
        return get(EXPLICIT_EXTERNAL);
    }

    @Override
    public String getStmtCode() {
        var prefix = keyword(FortranKeyword.IMPLICIT) + " " + keyword(FortranKeyword.NONE);

        var typeCode = explicitTypes() ? keyword(FortranKeyword.TYPE) : "";
        var externalCode = explicitExternal() ? keyword(FortranKeyword.EXTERNAL) : "";

        var specsCode = Stream.of(typeCode, externalCode)
                .filter(code -> !code.isEmpty())
                .collect(Collectors.joining(", "));

        return specsCode.isEmpty()
                ? prefix
                : prefix + " (" + specsCode + ")";
    }
}
