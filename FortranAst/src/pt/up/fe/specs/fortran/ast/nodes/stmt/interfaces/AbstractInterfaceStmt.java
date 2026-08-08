package pt.up.fe.specs.fortran.ast.nodes.stmt.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.GenericSpec;

import java.util.Collection;
import java.util.Optional;

public class AbstractInterfaceStmt extends InterfaceStmt {
    public AbstractInterfaceStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public Optional<GenericSpec> getGenericSpec() {
        return Optional.empty();
    }

    @Override
    public String getStmtCode() {
        return keyword(FortranKeyword.ABSTRACT) + " " + keyword(FortranKeyword.INTERFACE);
    }
}
