package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.utilities.PrintOnce;

import java.util.Collection;
import java.util.Optional;

public abstract class Stmt extends FortranNode {

    public Stmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<LabelDecl> getLabel() {
        var labelDecls = getChildrenOf(LabelDecl.class);
        return SpecsCollections.toOptional(labelDecls);
    }

    public String getStmtCode() {
        PrintOnce.info("getStmtCode() not implemented for nodes of type " + getClass());
        return "\n/*<.getStmtCode() not implemented for node " + this.getClass() + ">*/";
    }

    @Override
    public final String getCode() {
        var labelPrefix = getLabel().map(label -> label.getCode() + " ").orElse("");
        return labelPrefix + getStmtCode();
    }
}
