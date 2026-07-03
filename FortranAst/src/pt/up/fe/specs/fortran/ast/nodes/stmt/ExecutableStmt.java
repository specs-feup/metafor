package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.utilities.PrintOnce;

import java.util.Collection;
import java.util.Optional;

/**
 * R514 executable-construct
 */
public abstract class ExecutableStmt extends Stmt {

    // DATAKEYS BEGIN

    /**
     * The original source of this statement.
     */
    public final static DataKey<String> SOURCE = KeyFactory.string("source");


    // DATAKEYS END

    public ExecutableStmt(DataStore data, Collection<? extends FortranNode> children) {
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
