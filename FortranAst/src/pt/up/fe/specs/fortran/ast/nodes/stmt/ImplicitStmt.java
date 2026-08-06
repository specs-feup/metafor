package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.LetterSpec;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DeclType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ImplicitStmt extends ImplicitPartStmt {
    public ImplicitStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DeclType getDeclType() {
        return getChild(DeclType.class, 0);
    }

    public List<LetterSpec> getLetterSpecs() {
        return getChildrenOf(LetterSpec.class);
    }

    @Override
    public String getStmtCode() {
        var declTypeCode = getDeclType().getCode();
        var letterSpecsCode = getLetterSpecs().stream()
                .map(LetterSpec::getCode)
                .collect(Collectors.joining(", ", " (", ")"));

        return keyword(FortranKeyword.IMPLICIT) + " " + declTypeCode + letterSpecsCode;
    }
}
