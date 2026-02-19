package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * R1134 if-construct
 */
public class IfConstruct extends ExecutableStmt {
    public static final DataKey<Optional<String>> NAME = KeyFactory.optional("name");

    public IfConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IfThenBlock getIfThenBlock() {
        return getChild(IfThenBlock.class, 0);
    }

    public List<ElseIfBlock> getElseIfBlocks() {
        return getChildrenOf(ElseIfBlock.class);
    }

    public Optional<ElseBlock> getElseBlock() {
        return getChildTry(ElseBlock.class, getNumChildren() - 2);
    }

    public EndIfStmt getEndIfStmt() {
        return getChild(EndIfStmt.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var ifThenBlock = getIfThenBlock();
        var elseIfBlocks = getElseIfBlocks();
        var elseBlock = getElseBlock();
        var endIfStmt = getEndIfStmt();

        var code = new StringBuilder();

        // if-then block
        code.append(ifThenBlock.getCode());

        // else-if blocks
        elseIfBlocks.forEach(elseIfBlock -> code.append(ln()).append(elseIfBlock.getCode()));

        // else block
        elseBlock.ifPresent(block -> code.append(ln()).append(block.getCode()));

        // end if statement
        code.append(ln()).append(endIfStmt.getCode());

        return code.toString();
    }
}
