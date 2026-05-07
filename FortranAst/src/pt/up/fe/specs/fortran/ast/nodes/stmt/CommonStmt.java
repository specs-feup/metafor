package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommonStmt extends SpecificationStmt {

    public static DataKey<Boolean> HAS_NAME = KeyFactory.bool("has_name");

    public CommonStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<DataRef> getName() {
        return get(HAS_NAME) ? Optional.ofNullable(getChild(DataRef.class, 0)) : Optional.empty();
    }

    public List<DataRef> getNames() {
        return getChildren(DataRef.class, get(HAS_NAME) ? 1 : 0);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(FortranKeyword.COMMON.getKeyword(false));

        getName().ifPresent(name -> code.append("/ ").append(name.getCode()).append(" /"));

        code.append(getNames()
                .stream()
                .map(DataRef::getCode)
                .collect(Collectors.joining(", "))
        );

        return code.toString();
    }
}
