package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class VariableProcessor extends ANodeProcessor {
    public VariableProcessor(FortranJsonResult data) {
        super(data);
    }

    public void dataRefProcessor(DataRef dataRef) {

        String name = attributes(dataRef).getString("source");

        dataRef.set(DataRef.NAME, name);
    }
}
