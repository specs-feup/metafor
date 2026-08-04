package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalStmt extends SpecificationStmt {
    public ExternalStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataRef> getNames() {
        return getChildrenOf(DataRef.class);
    }

    @Override
    public String getStmtCode() {
        StringBuilder code = new StringBuilder();

        code.append(FortranKeyword.EXTERNAL.getKeyword(false)).append(" ");

        code.append(getNames()
                .stream()
                .map(DataRef::getCode)
                .collect(Collectors.joining(", "))
        );

        return code.toString();
    }
}
