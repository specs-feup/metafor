package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.utils.Format;
import pt.up.fe.specs.fortran.ast.nodes.io.IoControlSpec;
import pt.up.fe.specs.fortran.ast.nodes.utils.IoUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WriteStmt extends ActionStmt {
    public WriteStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<IoUnit> getIoUnit() {
        return getChildTry(IoUnit.class);
    }

    public Optional<Format> getFormat() {
        return getChildTry(Format.class);
    }

    public List<IoControlSpec> getControlSpecs() {
        return getChildrenOf(IoControlSpec.class);
    }

    public List<Expr> getOutputItems() {
        return getChildrenOf(Expr.class);
    }

    @Override
    public String getStmtCode() {
        StringBuilder code = new StringBuilder();
        code.append(FortranKeyword.WRITE).append("(");

        List<String> parts = new ArrayList<>();

        getIoUnit().ifPresent(unit -> parts.add(unit.getCode()));

        getFormat().ifPresent(fmt -> parts.add(fmt.getCode()));

        getControlSpecs().forEach(spec -> parts.add(spec.getCode()));

        code.append(String.join(", ", parts));

        code.append(") ");

        code.append(
                getOutputItems()
                        .stream()
                        .map(Expr::getCode)
                        .collect(Collectors.joining(", "))
        );

        return code.toString();
    }
}
