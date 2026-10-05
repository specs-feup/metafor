package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InputImpliedDoItem extends InputItem {
    public InputImpliedDoItem(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<InputItem> getItems() {
        return getChildren(InputItem.class);
    }

    public RangeLoopControl getControl() {
        return getChild(RangeLoopControl.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var itemsCode = getItems().stream()
                .map(InputItem::getCode)
                .collect(Collectors.joining(", "));

        var controlCode = getControl().getCode();

        return "(" + itemsCode + ", " + controlCode + ")";
    }
}
