package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.construct.SpecConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.typedef.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DerivedTypeDef extends SpecConstruct {
    public DerivedTypeDef(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getTypeName() {
        return getDerivedTypeStmt().getTypeName();
    }

    public DerivedTypeStmt getDerivedTypeStmt() {
        return getChild(DerivedTypeStmt.class, 0);
    }

    public List<TypeParamDefStmt> getTypeParamDefStmts() {
        return getChildrenOf(TypeParamDefStmt.class);
    }

    public List<PrivateOrSequenceStmt> getPrivateOrSequenceStmts() {
        return getChildrenOf(PrivateOrSequenceStmt.class);
    }

    public List<ComponentDefStmt> getComponentDefStmts() {
        return getChildrenOf(ComponentDefStmt.class);
    }

    public Optional<TypeBoundProcedurePart> getTypeBoundProcedurePart() {
        return getChildTry(TypeBoundProcedurePart.class, getNumChildren() - 2);
    }

    public EndTypeStmt getEndTypeStmt() {
        return getChild(EndTypeStmt.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var derivedTypeStmtCode = getDerivedTypeStmt().getCode();

        var typeParamDefStmtsCode = getTypeParamDefStmts().stream()
                .map(stmt -> indent(stmt.getCode()) + ln())
                .collect(Collectors.joining());

        var privateOrSequenceStmtsCode = getPrivateOrSequenceStmts().stream()
                .map(stmt -> indent(stmt.getCode()) + ln())
                .collect(Collectors.joining());

        var componentDefStmtsCode = getComponentDefStmts().stream()
                .map(stmt -> indent(stmt.getCode()) + ln())
                .collect(Collectors.joining());

        var typeBoundProcedurePart = getTypeBoundProcedurePart()
                .map(part -> part.getCode() + ln())
                .orElse("");

        var endDerivedTypeStmtCode = getEndTypeStmt().getCode();

        return derivedTypeStmtCode + ln() +
                typeParamDefStmtsCode + privateOrSequenceStmtsCode + componentDefStmtsCode +
                typeBoundProcedurePart +
                endDerivedTypeStmtCode;
    }
}
