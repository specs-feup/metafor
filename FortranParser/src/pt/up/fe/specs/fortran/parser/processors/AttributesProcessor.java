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
        var keyword = attributes(keywordSpecifier).getString("keyword");

        keywordSpecifier.set(KeywordAttributeSpecifier.KEYWORD, keyword);
    }
}
