package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.*;

public class Function extends ProgramUnit {

    public final static DataKey<String> FUNCTION_NAME = KeyFactory.string("programName");

    public Function(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataRef> getArgs() {
        return getChildrenOf(DataRef.class);
    }

    @Override
    public String getCode() {

        var code = new StringBuilder();

        var argCode = "(" +
                getArgs().stream()
                        .map(DataRef::getCode)
                        .collect(Collectors.joining(", ")) +
                ")";

        var functionName = get(FUNCTION_NAME);

        code.append(keyword(FUNCTION)).append(" ").append(functionName).append(argCode).append(ln());

        code.append(getBodyCode()).append(ln());

        code.append(keyword(END));
        code.append(" ").append(keyword(FUNCTION)).append(" ").append(functionName);
        code.append(ln());

        return code.toString();
    }
}
