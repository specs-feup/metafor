package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.IntentSpec;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.KeywordAttributeSpecifier;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.enums.IntentKind;
import pt.up.fe.specs.fortran.parser.FlangData;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.EnumSet;
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
        var variantKey = attributes(arraySpecification).getString(FlangData.VARIANT_IDENTIFIER_KEY);
        var shapes = getChildren(arraySpecification, variantKey);
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
}
