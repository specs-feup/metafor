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

    public final static DataKey<String> SUBROUTINE_NAME = KeyFactory.string("programName");

    public Subroutine(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DummyArgumentDecl> getDummyArgs() {
        return getChildrenOf(DummyArgumentDecl.class);
    }

    public String getName() {
        return get(SUBROUTINE_NAME);
    }

    @Override
    public String getCode() {

        var code = new StringBuilder();

        var argCode = "(" +
                getDummyArgs().stream()
                        .map(DummyArgumentDecl::getCode)
                        .collect(Collectors.joining(", ")) +
                ")";

        var subroutineName = get(SUBROUTINE_NAME);

        code.append(keyword(SUBROUTINE)).append(" ").append(subroutineName).append(argCode).append(ln());

        code.append(getBodyCode()).append(ln());

        code.append(keyword(END));
        code.append(" ").append(keyword(SUBROUTINE)).append(" ").append(subroutineName);
        code.append(ln());

        return code.toString();
    }
}
