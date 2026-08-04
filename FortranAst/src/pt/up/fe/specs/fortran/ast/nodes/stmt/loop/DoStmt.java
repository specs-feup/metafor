package pt.up.fe.specs.fortran.ast.nodes.stmt.loop;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.Optional;

public class DoStmt extends Stmt {
    public static final DataKey<Optional<String>> DO_LABEL = KeyFactory.optional("do_label");

    public DoStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<LoopControl> getLoopControl() {
        return getChildOf(LoopControl.class);
    }

    public Optional<String> getDoLabel() {
        return get(DO_LABEL);
    }

    @Override
    public String getStmtCode() {
        var doNameOpt = getAncestor(DoConstruct.class).getName();
        var doLabelOpt = getDoLabel();
        var loopControlOpt = getLoopControl();

        var code = new StringBuilder();

        doNameOpt.ifPresent(doName -> code.append(doName).append(" : "));

        code.append(keyword(FortranKeyword.DO));

        doLabelOpt.ifPresent(doLabel -> code.append(" ").append(doLabel));

        loopControlOpt.ifPresent(loopControl -> code.append(" ").append(loopControl.getCode()));

        return code.toString();
    }
}
