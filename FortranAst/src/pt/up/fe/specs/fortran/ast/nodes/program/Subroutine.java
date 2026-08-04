package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.DummyArgumentDecl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.*;

public class Subroutine extends ProgramUnit {

    public final static DataKey<String> NAME = KeyFactory.string("name");

    public Subroutine(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    public SubroutineStmt getSubroutineStmt() {
        return getChild(SubroutineStmt.class);
    }

    public EndSubroutineStmt getEndSubroutineStmt() {
        return getChild(EndSubroutineStmt.class);
    }

    @Override
    public String getCode() {
        var subroutineStmt = getSubroutineStmt();
        var endSubroutineStmt = getEndSubroutineStmt();

        return subroutineStmt.getCode() + ln() + getBodyCode() + ln() + endSubroutineStmt.getCode();
    }
}
