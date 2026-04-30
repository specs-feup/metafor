package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * Represents Fortran integer types.
 */
public class LogicalType extends IntrinsicType {

    public LogicalType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return "logical";
    }
}
