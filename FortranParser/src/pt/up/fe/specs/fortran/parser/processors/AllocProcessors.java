package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;
import pt.up.fe.specs.fortran.ast.nodes.alloc.StatAllocOption;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class AllocProcessors extends ANodeProcessor {
    public AllocProcessors(FortranJsonResult data) {
        super(data);
    }

    public void allocation(Allocation allocation) {
        allocation.addChild(getChild(allocation, FlangName.ALLOCATE_OBJECT));
        allocation.addChildren(getChildren(allocation, FlangName.ALLOCATE_SHAPE_SPEC));
    }

    public void statAllocOption(StatAllocOption statAllocOption) {
        statAllocOption.addChild(getChild(statAllocOption, FlangName.EXPR));
    }
}
