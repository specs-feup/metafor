package pt.up.fe.specs.fortran.ast.nodes.stmt.loop;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.enums.DoKind;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.Optional;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.DO;
import static pt.up.fe.specs.fortran.ast.FortranKeyword.END;

public class DoConstruct extends ExecutableStmt {

    public static final DataKey<Optional<String>> NAME = KeyFactory.optional("name");

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

        return doStmt.getCode() + ln() + indent(body.getCode()) + ln() + endDoStmt.getCode();
    }
}
