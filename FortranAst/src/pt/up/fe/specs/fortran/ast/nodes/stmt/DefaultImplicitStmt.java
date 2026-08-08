package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.ImplicitSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.LetterSpec;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DeclType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultImplicitStmt extends ImplicitStmt {
    public DefaultImplicitStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<ImplicitSpec> getSpecs() {
        return getChildren(ImplicitSpec.class);
    }

    @Override
    public String getStmtCode() {
        var specsCode = getSpecs().stream()
                .map(ImplicitSpec::getCode)
                .collect(Collectors.joining(", "));

        return keyword(FortranKeyword.IMPLICIT) + " " + specsCode;
    }
}
