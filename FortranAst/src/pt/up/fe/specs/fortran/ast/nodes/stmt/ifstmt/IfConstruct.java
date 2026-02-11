package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;

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

    @Override
    public String getCode() {
        var ifThenBlock = getIfThenBlock();

        var code = new StringBuilder();
        code.append(ifThenBlock.getCode());

        code.append(keyword(FortranKeyword.END))
                .append(" ")
                .append(keyword(FortranKeyword.IF))
                .append(ln());

        return code.toString();
    }
}
