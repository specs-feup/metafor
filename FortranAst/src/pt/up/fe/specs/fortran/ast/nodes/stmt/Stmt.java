package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.utilities.PrintOnce;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Stmt extends FortranNode {
    public static final DataKey<List<String>> COMMENTS = KeyFactory.list("comments", String.class);

    public Stmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    // TODO(Process-ing): Convert this to data property
    public Optional<LabelDecl> getLabel() {
        var labelDecls = getChildrenOf(LabelDecl.class);
        return SpecsCollections.toOptional(labelDecls);
    }

    public List<String> getComments() {
        return get(COMMENTS);
    }

    public String getStmtCode() {
        PrintOnce.info("getStmtCode() not implemented for nodes of type " + getClass());
        return "\n/*<.getStmtCode() not implemented for node " + this.getClass() + ">*/";
    }

    @Override
    public final String getCode() {
        var labelPrefix = getLabel().map(label -> label.getCode() + " ").orElse("");

        var comments = getComments();
        SpecsCheck.checkArgument(comments != null, () -> "Comment array not initialized in node of class \""
                + getClass() + "\". Make sure you call the method StmtProcessors::stmt() on the processor for this "
                + "node kind to initialize comments and labels.");

        var commentsPrefix = comments.stream()
                .map(comment -> comment + ln())
                .collect(Collectors.joining());

        return commentsPrefix + labelPrefix + getStmtCode();
    }
}
