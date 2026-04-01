package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.decl.DummyArgumentDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.parser.FlangData;
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
            var init = getChild(entityDecl, FlangName.INITIALIZATION);
            entityDecl.addChild(init);
        }

    }

    public void dummyArgumentDecl(DummyArgumentDecl dummyArgumentDecl) {
        var variantKey = attributes(dummyArgumentDecl).getString(FlangData.VARIANT_IDENTIFIER_KEY);
        var nameId = attributes(dummyArgumentDecl).getString(variantKey);
        var name = attributes().get(nameId).getString("source");

        dummyArgumentDecl.set(DummyArgumentDecl.NAME, name);
    }
}
