package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.specification.NamedConstantDef;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.IntentSpec;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.KeywordAttributeSpecifier;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.enums.IntentKind;
import pt.up.fe.specs.fortran.parser.FlangData;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class AttributesProcessor extends ANodeProcessor {
    private static final Set<FlangName> KEYWORD_ATTRIBUTES = EnumSet.of(
            FlangName.ALLOCATABLE,
            FlangName.ASYNCHRONOUS
    );

    public AttributesProcessor(FortranJsonResult data) {
        super(data);
    }

    public void arraySpecification(ArraySpecification arraySpecification) {
        var variantKey = attributes(arraySpecification).getVariantKey();
        List<FortranNode> shapes;

        if (variantKey.equals(FlangName.ASSUMED_SIZE_SPEC.getString())) {
            String childSpec = attributes(arraySpecification).getVariantString();
            shapes = getChildren(childSpec, FlangName.EXPLICIT_SHAPE_SPEC);
            shapes.add(getChild(attributes().get(childSpec).getString(FlangName.ASSUMED_IMPLIED_SPEC)));
        }
        else {
            shapes = getChildren(arraySpecification, variantKey);
        }
        arraySpecification.addChildren(shapes);
    }

    public void keywordSpecifier(KeywordAttributeSpecifier keywordSpecifier) {
        var keyword = attributes(keywordSpecifier).getString("keyword");

        keywordSpecifier.set(KeywordAttributeSpecifier.KEYWORD, keyword);
    }

    public void intentSpec(IntentSpec intentSpec) {
        intentSpec.set(
                IntentSpec.KIND,
                IntentKind.convertTry(attributes(intentSpec).getString("intent")).get()
        );
    }

    public void namedConstantDef(NamedConstantDef namedConstantDef) {
        namedConstantDef.addChild(getChild(namedConstantDef, FlangName.NAMED_CONSTANT));
        namedConstantDef.addChild(getChild(namedConstantDef, FlangName.EXPR));
    }
}
