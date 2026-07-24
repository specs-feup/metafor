package pt.up.fe.specs.fortran.ast.nodes.type.lenselector;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.typeparam.TypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class KindParamLenSelector extends LenSelector {
    public KindParamLenSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getKind() {
        return getChild(Expr.class, 0);
    }

    public Optional<TypeParamValue> getLength() {
        return getChildTry(TypeParamValue.class, 1);
    }

    @Override
    public String getCode() {
        var kindCode = keyword(FortranKeyword.KIND) + "=" + getKind().getCode();
        var lenCode = getLength()
                .map(len -> ", LEN=" + len.getCode())
                .orElseGet(() -> "");

        return "(" + kindCode + lenCode + ")";
    }
}
