package pt.up.fe.specs.fortran.ast.nodes.utils;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * R1215 format
 */
// TODO(Process-ing): Rewrite this node to have better type consistency
public class Format extends FortranNode {

    public Format(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public FortranNode getFormatType() {
        return getChild(0);
    }

    @Override
    public String getCode() {
        return getFormatType().getCode();
    }
}
