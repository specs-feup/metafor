package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.decl.DesignatorVariable;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.ScopeKind;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class VariableProcessor extends ANodeProcessor {
    public VariableProcessor(FortranJsonResult data) {
        super(data);
    }

    public void dataRef(DataRef dataRef) {
        var name = attributes(dataRef).getString("source");var scope = attributes(dataRef)
                .getOptionalString("scope")
                .flatMap(ScopeKind::of);
        dataRef.set(DataRef.NAME, name);
        dataRef.set(DataRef.SCOPE, scope);
    }

    public void designatorVariable(DesignatorVariable variable) {
        var designator = getChild(variable, FlangName.DESIGNATOR);
        variable.addChild(designator);
    }
}
