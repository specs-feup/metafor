package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.decl.DummyArgumentDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.Initialization;
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
            // If the child is a DataStmtValue, we build a ListInitialization
            throw new UnsupportedOperationException("DataStmtValue initialization is not supported yet");
        }

        // Otherwise, we assume it's an ExprInitialization
        var childId = attrs.getString(variantKey);
        var exprInit = getChild(childId);

        var initialization = factory().exprInitialization();
        initialization.addChild(exprInit);

        return initialization;
    }

    public void dummyArgumentDecl(DummyArgumentDecl dummyArgumentDecl) {
        var nameId = attributes(dummyArgumentDecl).getVariantString();
        var name = attributes().get(nameId).getString("source");

        dummyArgumentDecl.set(DummyArgumentDecl.NAME, name);
    }
}
