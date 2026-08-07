package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.NamedParameter;
import pt.up.fe.specs.fortran.ast.nodes.specification.LanguageBindingSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.funcspec.FunctionSpec;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO(Process-ing): Include support for `prefix`
public class FunctionStmt extends Stmt {
    public static final DataKey<String> FUNCTION_NAME = KeyFactory.string("function_name");
    public static final DataKey<Optional<String>> RESULT_NAME = KeyFactory.optional("result_name");

    public FunctionStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<FunctionSpec> getFunctionSpecs() {
        return getChildrenOf(FunctionSpec.class);
    }

    public String getFunctionName() {
        return get(FUNCTION_NAME);
    }

    public List<NamedParameter> getParameters() {
        return getChildrenOf(NamedParameter.class);
    }

    public Optional<LanguageBindingSpec> getBinding() {
        return getChildOf(LanguageBindingSpec.class);
    }

    public Optional<String> getResultName() {
        return get(RESULT_NAME);
    }

    @Override
    public String getStmtCode() {
        var prefix = getFunctionSpecs().stream()
                .map(spec -> spec.getCode() + " ")
                .collect(Collectors.joining());

        var functionName = getFunctionName();

        var argCode = getParameters().stream()
                .map(NamedParameter::getCode)
                .collect(java.util.stream.Collectors.joining(", ", "(", ")"));

        var bindingCode = getBinding()
                .map(binding -> " " + binding)
                .orElse("");
        var resultNameCode = getResultName()
                .map(name -> " " + keyword(FortranKeyword.RESULT) + "(" + name + ")")
                .orElse("");
        var suffix = resultNameCode + bindingCode;

        return prefix + keyword(FortranKeyword.FUNCTION) + " " + functionName + argCode + suffix;
    }
}
