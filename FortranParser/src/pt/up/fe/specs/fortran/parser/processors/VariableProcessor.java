package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class VariableProcessor extends ANodeProcessor {
    public VariableProcessor(FortranJsonResult data) {
        super(data);
    }

    public void dataRefProcessor(DataRef dataRef) {

        var name = attributes(dataRef).getString("source");
        var scope = attributes(dataRef).getOptionalString("scope");

        dataRef.set(DataRef.NAME, name);
        dataRef.set(DataRef.SCOPE, scope);
    }
}
