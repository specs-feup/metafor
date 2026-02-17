package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.Optional;

/**
 * R1134 if-construct
 */
public class IfConstruct extends ExecutableStmt {
    public IfConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IfThenBlock getIfThenBlock() {
        return getChild(IfThenBlock.class, 0);
    }

    public Optional<ElseBlock> getElseBlock() {
        return getChildTry(ElseBlock.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var ifThenBlock = getIfThenBlock();
        var elseBlock = getElseBlock();

        System.out.println(getChildren());

        var code = new StringBuilder();

        code.append(ifThenBlock.getCode());

        elseBlock.ifPresent(block -> code.append(block.getCode()));

        code.append(keyword(FortranKeyword.END))
                .append(" ")
                .append(keyword(FortranKeyword.IF))
                .append(ln());

        return code.toString();
    }
}
