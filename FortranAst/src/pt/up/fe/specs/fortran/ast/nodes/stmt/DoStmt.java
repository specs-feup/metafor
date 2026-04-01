package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.enums.DoKind;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;

import java.util.Collection;
import java.util.Optional;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.DO;
import static pt.up.fe.specs.fortran.ast.FortranKeyword.END;

public class DoStmt extends ExecutableStmt {

    public static final DataKey<Optional<String>> NAME = KeyFactory.optional("name");

    public DoStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<LoopControl> getControl() {
        return getChildTry(LoopControl.class);
    }

    public Execution getBody() {
        return getChild(Execution.class);
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
    public String getCode() {
        var code = new StringBuilder();
        Optional<String> name = getName();

        name.ifPresent(str -> code.append(str).append(": "));

        code.append(keyword(DO)).append(" ").append(getControlCode()).append(ln());

        code.append(getBody().getCode()).append(ln());

        code.append(keyword(END)).append(" ").append(keyword(DO));

        name.ifPresent(str -> code.append(" ").append(str));

        return code.toString();
    }
}
