package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DeclType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ImplicitSpec extends FortranNode {
    public ImplicitSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DeclType getDeclType() {
        return getChild(DeclType.class, 0);
    }

    public List<LetterSpec> getLetterSpecs() {
        return getChildrenOf(LetterSpec.class);
    }

    @Override
    public String getCode() {
        var declTypeCode = getDeclType().getCode();
        var letterSpecsCode = getLetterSpecs().stream()
                .map(LetterSpec::getCode)
                .collect(Collectors.joining(", ", " (", ")"));

        return declTypeCode + letterSpecsCode;
    }
}
