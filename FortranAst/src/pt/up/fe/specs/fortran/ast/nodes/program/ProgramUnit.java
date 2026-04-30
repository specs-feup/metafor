package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * R502 program-unit
 */
public abstract class ProgramUnit extends FortranNode {
    public ProgramUnit(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getBodyCode() {
        /*
        // Write code of the children, indented
        var code = new StringBuilder();
        for(var stmt : getChildren()) {

            code.append(tab());

            // If statement has a LabelDecl, print it

        }
        */
        return getChildren().stream()
                .filter(node -> (node instanceof StmtBlock) || (node instanceof InternalSubprogram))
                .map(FortranNode::getCode)
                .filter(code -> !code.isEmpty())
                .map(this::indent)
                .collect(Collectors.joining(ln()));

        //return code.toString();
    }
}
