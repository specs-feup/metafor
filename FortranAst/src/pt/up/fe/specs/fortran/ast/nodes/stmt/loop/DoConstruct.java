package pt.up.fe.specs.fortran.ast.nodes.stmt.loop;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.enums.DoKind;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ContinueStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.Optional;

public class DoConstruct extends ExecutableStmt {
    public static final DataKey<Optional<String>> NAME = KeyFactory.optional("name");
    public static final DataKey<Optional<String>> DO_LABEL = KeyFactory.optional("doLabel");

    public DoConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DoStmt getDoStmt() {
        return getChild(DoStmt.class);
    }

    public Execution getBody() {
        return getChild(Execution.class);
    }

    public EndDoStmt getEndDoStmt() {
        return getChild(EndDoStmt.class);
    }

    public Optional<LoopControl> getControl() {
        return getDoStmt().getLoopControl();
    }

    public Optional<String> getName() {
        return get(NAME);
    }

    public Optional<String> getDoLabel() {
        return get(DO_LABEL);
    }

    public DoKind getKind() {
        return getControl()
                .map(DoKind::fromControl)
                .orElse(DoKind.WHILE);
    }

    private String getControlCode() {
        return getControl()
                .map(LoopControl::getCode)
                .orElse("");
    }

    @Override
    public String getStmtCode() {
        var doStmt = getDoStmt();
        var body = getBody();
        var endDoStmt = getEndDoStmt();

        var code = new StringBuilder();

        code.append(doStmt.getCode()).append(ln())
                .append(body.getCode());

        var endDoStmtCode = endDoStmt.getStmtCode();
        if (!endDoStmtCode.isEmpty()) {
            code.append(ln()).append(endDoStmtCode);
        }

        return code.toString();
    }

    public boolean hasContinueEnd() {
        var doLabelOpt = getDoLabel();
        if (doLabelOpt.isEmpty()) {
            return false;
        }

        var doLabel = doLabelOpt.get();
        var lastStmt = getBody().getChildTry(getNumChildren() - 1);
        if (lastStmt.isEmpty() || !(lastStmt.get() instanceof ContinueStmt contLastStmt)) {
            return false;
        }

        var contLabel = contLastStmt.getLabel().map(LabelDecl::getCode);
        return contLabel.map(doLabel::equals).orElse(false);
    }
}
