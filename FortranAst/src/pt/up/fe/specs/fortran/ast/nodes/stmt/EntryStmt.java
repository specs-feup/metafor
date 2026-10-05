package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Parameter;
import pt.up.fe.specs.fortran.ast.nodes.specification.LanguageBindingSpec;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class EntryStmt extends IDEStmt {
    public static final DataKey<String> ENTRY_NAME = KeyFactory.string("entry_name");
    public static final DataKey<Optional<String>> RESULT_NAME = KeyFactory.optional("result_name");

    public EntryStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getEntryName() {
        return get(ENTRY_NAME);
    }

    public List<Parameter> getParameters() {
        return getChildrenOf(Parameter.class);
    }

    public Optional<LanguageBindingSpec> getBinding() {
        return getChildTry(LanguageBindingSpec.class, getNumChildren() - 1);
    }

    public Optional<String> getResultName() {
        return get(RESULT_NAME);
    }

    @Override
    public String getStmtCode() {
        var entryName = getEntryName();
        var parametersCode = getParameters().stream()
                .map(Parameter::getCode)
                .collect(java.util.stream.Collectors.joining(", ", "(", ")"));

        var bindingCode = getBinding()
                .map(binding -> " " + binding.getCode())
                .orElse("");
        var resultNameCode = getResultName()
                .map(resultName -> " " + keyword(FortranKeyword.RESULT) + "(" + resultName + ")")
                .orElse("");
        var suffix = bindingCode + resultNameCode;

        return keyword(FortranKeyword.ENTRY) + " " + entryName + parametersCode + suffix;
    }
}
