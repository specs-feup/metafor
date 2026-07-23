package pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.Optional;

public abstract class UseStmt extends Stmt {
    public static final DataKey<Optional<Boolean>> INTRINSIC = KeyFactory.optional("intrinsic");
    public static final DataKey<String> NAME = KeyFactory.string("name");

    public UseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Boolean> isIntrinsic() {
        return get(INTRINSIC);
    }

    public String getName() {
        return get(NAME);
    }

    protected String getUseStmtPrefix() {
        var intrinsic = isIntrinsic();
        var name = getName();

        var code = new StringBuilder();

        code.append(keyword(FortranKeyword.USE));

        intrinsic.ifPresentOrElse(intrinsicVal -> {
            var intrinsicCode = keyword(intrinsicVal ? FortranKeyword.INTRINSIC : FortranKeyword.NON_INTRINSIC);

            code.append(", ").append(intrinsicCode).append(" :: ");
        }, () -> {
            code.append(!fixedForm() ? " :: " : " ");
        });

        code.append(name);

        return code.toString();
    }
}
