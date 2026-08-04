package pt.up.fe.specs.fortran.ast.nodes.type.attributes;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ParameterKeyword extends KeywordAttributeSpecifier {
    public ParameterKeyword(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
