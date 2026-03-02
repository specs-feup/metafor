package pt.up.fe.specs.fortran.ast.nodes.type.attributes;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class AllocatableKeyword extends KeywordAttributeSpecifier{
    public AllocatableKeyword(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
