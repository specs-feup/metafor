package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.utilities.PrintOnce;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Stmt extends FortranNode {

    public Stmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<LabelDecl> getLabel() {
        var labelDecls = getChildrenOf(LabelDecl.class);
        return SpecsCollections.toOptional(labelDecls);
    }

    public List<CommentStmt> getComments() {
        return getChildrenOf(CommentStmt.class);
    }

    public String getStmtCode() {
        PrintOnce.info("getStmtCode() not implemented for nodes of type " + getClass());
        return "\n/*<.getStmtCode() not implemented for node " + this.getClass() + ">*/";
    }

    @Override
    public final String getCode() {
        var labelPrefix = getLabel().map(label -> label.getCode() + " ").orElse("");

        var comments = getComments();
        var commentsPrefix = comments.stream()
                .map(comment -> comment.getCode() + ln())
                .collect(Collectors.joining());

        return commentsPrefix + labelPrefix + getStmtCode();
    }
}
