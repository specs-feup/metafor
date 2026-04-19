package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.type.*;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class TypeProcessors extends ANodeProcessor {


    public TypeProcessors(FortranJsonResult data) {
        super(data);
    }

    public void integerType(IntegerType integerType) {

    }

    public void logicalType(LogicalType logicalType) {

    }

    public void doublePrecisionType(DoublePrecisionType doublePrecisionType) {

    }

    public void characterType(CharacterType characterType) {
        if (attributes(characterType).has(FlangName.CHAR_SELECTOR)) {
            characterType.addChild(getChild(characterType, FlangName.CHAR_SELECTOR));
        }
    }

    public void realType(RealType realType) {

    }

    public void lengthSelector(LengthSelector lengthSelector) {
        var childId = attributes(lengthSelector).getVariantString();
        lengthSelector.addChild(getChild(attributes().get(childId).getVariantString()));
    }
}
