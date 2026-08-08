package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.InternalSubprogram;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AInternalSubprogram;

public class FInternalSubprogram extends AInternalSubprogram {
    private final InternalSubprogram internalSubprogram;

    public FInternalSubprogram(InternalSubprogram internalSubprogram) {
        super(new FSubprogram(internalSubprogram));

        this.internalSubprogram = internalSubprogram;
    }

    @Override
    public FortranNode getNode() {
        return internalSubprogram;
    }
}
