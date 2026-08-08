package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.enums.ImportKind;

import java.util.Collection;
import java.util.List;

public class ImportStmt extends Stmt {
    public static final DataKey<ImportKind> KIND = KeyFactory.enumeration("kind", ImportKind.class);
    public static final DataKey<List<String>> NAMES = KeyFactory.list("names", String.class);

    public ImportStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ImportKind getKind() {
        return get(KIND);
    }

    public List<String> getImportedNames() {
        return get(NAMES);
    }

    @Override
    public String getStmtCode() {
        var names = getImportedNames();

        var suffix = switch (getKind()) {
            case DEFAULT -> names.isEmpty() ? "" : " :: " + String.join(", ", names);
            case ONLY -> ", " + keyword(FortranKeyword.ONLY) + " : " + String.join(", ", names);
            case NONE -> ", " + keyword(FortranKeyword.NONE);
            case ALL -> ", " + keyword(FortranKeyword.ALL);
        };

        return keyword(FortranKeyword.IMPORT) + suffix;
    }
}
