package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.decl.KindSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.*;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.ParamLenSelector;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class TypeProcessors extends ANodeProcessor {
    public TypeProcessors(FortranJsonResult data) {
        super(data);
    }

    public void integerType(IntegerType integerType) {
        if (attributes(integerType).has(FlangName.KIND_SELECTOR)) {
            var kindSelector = getChild(integerType, FlangName.KIND_SELECTOR);
            integerType.addChild(kindSelector);
        }
    }

    public void kindSelector(KindSelector kindSelector) {
        var variantKey = attributes(kindSelector).getVariantKey();

        if (variantKey.equals(FlangName.STAR_SIZE.getString())) {
            // This is a legacy kind selector (e.g. "integer*8")
            var value = attributes().getString(kindSelector, "uint64_t", FlangName.STAR_SIZE);

            kindSelector.set(KindSelector.VALUE, value);
            kindSelector.set(KindSelector.LEGACY, true);

        } else {
            // Otherwise, we assume it's a modern kind selector (e.g. "integer(8)")
            var value = attributes().getOptionalString(kindSelector, "CharBlock", FlangName.EXPR, FlangName.LITERAL_CONSTANT, FlangName.INT_LITERAL_CONSTANT)
                    .or(() -> attributes().getOptionalString(kindSelector, "source", FlangName.EXPR, FlangName.DESIGNATOR, FlangName.DATA_REF, FlangName.NAME));

            if (value.isEmpty()) {
                throw new RuntimeException("Could not find value for kind selector: " + kindSelector);
            }

            kindSelector.set(KindSelector.VALUE, value.get());
            kindSelector.set(KindSelector.LEGACY, false);
        }
    }

    public void logicalType(LogicalType logicalType) {
        if (attributes(logicalType).has(FlangName.KIND_SELECTOR)) {
            var kindSelector = getChild(logicalType, FlangName.KIND_SELECTOR);
            logicalType.addChild(kindSelector);
        }
    }

    public void doublePrecisionType(DoublePrecisionType doublePrecisionType) {

    }

    public void characterType(CharacterType characterType) {
        if (attributes(characterType).has(FlangName.CHAR_SELECTOR)) {
            characterType.addChild(getChild(characterType, FlangName.CHAR_SELECTOR));
        }
    }

    public void realType(RealType realType) {
        if (attributes(realType).has(FlangName.KIND_SELECTOR)) {
            var kindSelector = getChild(realType, FlangName.KIND_SELECTOR);
            realType.addChild(kindSelector);
        }
    }

    public void lengthSelector(ParamLenSelector lengthSelector) {
        var childId = attributes(lengthSelector).getVariantString();
        lengthSelector.addChild(getChild(attributes().get(childId).getVariantString()));
    }

    public void complexType(ComplexType complexType) {
        if (attributes(complexType).has(FlangName.KIND_SELECTOR)) {
            var kindSelector = getChild(complexType, FlangName.KIND_SELECTOR);
            complexType.addChild(kindSelector);
        }
    }
}
