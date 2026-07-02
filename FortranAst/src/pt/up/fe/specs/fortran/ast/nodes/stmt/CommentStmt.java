package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class CommentStmt extends Stmt {
    public static final DataKey<String> CONTENT = KeyFactory.string("content");

    public CommentStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getContent() {
        return get(CONTENT);
    }

    @Override
    public String getCode() {
        return getContent();
    }
}
