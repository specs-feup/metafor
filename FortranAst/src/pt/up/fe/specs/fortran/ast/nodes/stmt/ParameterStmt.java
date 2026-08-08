package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.NamedConstantDef;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ParameterStmt extends SpecStmt {
    public ParameterStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<NamedConstantDef> getDefs() {
        return getChildrenOf(NamedConstantDef.class);
    }

    @Override
    public String getStmtCode() {
        var defsCode = getDefs().stream()
                .map(NamedConstantDef::getCode)
                .collect(Collectors.joining(", "));

        return keyword(FortranKeyword.PARAMETER) + " (" + defsCode + ")";
    }
}
