package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.KeywordAttributeSpecifier;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

public class AttributesProcessor extends ANodeProcessor {
    private static final Set<FlangName> KEYWORD_ATTRIBUTES = EnumSet.of(
            FlangName.ALLOCATABLE,
            FlangName.ASYNCHRONOUS
    );

    public AttributesProcessor(FortranJsonResult data) {
        super(data);
    }


    public void arraySpecification(ArraySpecification arraySpecification) {
        var shapes = getChildren(arraySpecification, "value");
        arraySpecification.addChildren(shapes);
    }

    public void keywordSpecifier(KeywordAttributeSpecifier keywordSpecifier) {
        var keywordNodeId = attributes(keywordSpecifier).getString("id");
        var match = KEYWORD_ATTRIBUTES.stream()
                .filter(fName -> Pattern.compile(".*-" + fName.getString() + "$").matcher(keywordNodeId).matches())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unexpected keyword attribute with id '" + keywordNodeId + "'"));

        keywordSpecifier.set(KeywordAttributeSpecifier.KEYWORD, match.name());
    }
}
