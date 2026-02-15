package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.variable.DataRef;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class VariableProcessor extends ANodeProcessor {
    public VariableProcessor(FortranJsonResult data) {
        super(data);
    }

    public void dataRefProcessor(DataRef dataRef) {
        var nameId = attributes(dataRef).getString("value");
        var name = attributes().get(nameId).getString("source");

        dataRef.set(DataRef.NAME, name);
    }
}
