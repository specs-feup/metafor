package pt.up.fe.specs.fortran.ast.nodes.stmt.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.GenericSpec;

import java.util.Collection;
import java.util.Optional;

public class DefaultInterfaceStmt extends InterfaceStmt {
    public DefaultInterfaceStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public Optional<GenericSpec> getGenericSpec() {
        return getChildTry(GenericSpec.class, 0);
    }

    @Override
    public String getStmtCode() {
        var genericSpecCode = getGenericSpec()
                .map(spec -> spec.getCode() + " ")
                .orElse("");

        return keyword(FortranKeyword.INTERFACE) + genericSpecCode;
    }
}
