package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.decl.*;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class DeclProcessors extends ANodeProcessor {


    public DeclProcessors(FortranJsonResult data) {
        super(data);
    }

    public void entityDecl(EntityDecl entityDecl) {
        var nameId = attributes(entityDecl).getString("Name");
        var name = attributes().get(nameId).getString("source");

        entityDecl.set(EntityDecl.NAME, name);

        if (attributes(entityDecl).has(FlangName.ARRAY_SPEC)) {
            var arraySpec = getChild(entityDecl, FlangName.ARRAY_SPEC);
            entityDecl.addChild(arraySpec);
        }

        if (attributes(entityDecl).has(FlangName.INITIALIZATION)) {
            var initId = attributes(entityDecl).getString(FlangName.INITIALIZATION);
            var init = buildInitialization(initId);
            entityDecl.addChild(init);
        }
    }

    public Initialization buildInitialization(String id) {
        var attrs = attributes().getAttrs(id);
        var variantKey = attrs.getVariantKey();

        if (variantKey.equals(FlangName.DATA_STMT_VALUE.getString())) {
            var dataStmtValueIds = attrs.getStringList(FlangName.DATA_STMT_VALUE);
            var dataStmtValues = dataStmtValueIds.stream()
                    .map(this::getNode)
                    .toList();

            var init = factory().listInitialization();
            init.addChildren(dataStmtValues);

            return init;
        }

        // Otherwise, we assume it's an ExprInitialization
        var childId = attrs.getString(variantKey);
        var initExpr = getChild(childId);

        var init = factory().exprInitialization();
        init.addChild(initExpr);

        return init;
    }

    public void dataStmtValue(DataStmtValue dataStmtValue) {
        if (attributes(dataStmtValue).has(FlangName.DATA_STMT_REPEAT)) {
            var repeat = getChild(dataStmtValue, FlangName.DATA_STMT_REPEAT);
            dataStmtValue.addChild(repeat);
        }

        var constant = getChild(dataStmtValue, FlangName.DATA_STMT_CONSTANT);
        dataStmtValue.addChild(constant);
    }

    public void namedParameter(NamedParameter namedParameter) {
        var name = attributes().getString(namedParameter, "source", FlangName.NAME);
        namedParameter.set(NamedParameter.NAME, name);
    }

    public void starParameter(StarParameter starParameter) {

    }
}
