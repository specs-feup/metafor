package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OutputImpliedDoItem extends OutputItem {
    public OutputImpliedDoItem(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<OutputItem> getItems() {
        return getChildren(OutputItem.class);
    }

    public RangeLoopControl getControl() {
        return getChild(RangeLoopControl.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var itemsCode = getItems().stream()
                .map(OutputItem::getCode)
                .collect(Collectors.joining(", "));

        var controlCode = getControl().getCode();

        return "(" + itemsCode + ", " + controlCode + ")";
    }
}
