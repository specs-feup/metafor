package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.decl.KindSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.*;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DerivedDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.IntrinsicDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.StarDeclType;
import pt.up.fe.specs.fortran.ast.nodes.type.enums.DeclTypeKind;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.ConstLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.KindParamLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.ParamLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.TypeParam;
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

    public void constLenSelector(ConstLenSelector selector) {
        System.out.println(attributes(selector));
        var length = attributes().getString(selector, "uint64_t");
        selector.set(ConstLenSelector.LENGTH, Long.parseLong(length));
    }

    public void paramLenSelector(ParamLenSelector selector) {
        var param = getChild(selector, FlangName.TYPE_PARAM_VALUE);
        selector.addChild(param);
    }

    public void kindParamLenSelector(KindParamLenSelector selector) {
        var kind = getChild(selector, FlangName.EXPR);
        selector.addChild(kind);

        if (attributes(selector).has(FlangName.TYPE_PARAM_VALUE)) {
            var param = getChild(selector, FlangName.TYPE_PARAM_VALUE);
            selector.addChild(param);
        }
    }

    public void complexType(ComplexType complexType) {
        if (attributes(complexType).has(FlangName.KIND_SELECTOR)) {
            var kindSelector = getChild(complexType, FlangName.KIND_SELECTOR);
            complexType.addChild(kindSelector);
        }
    }

    public void typeParam(TypeParam typeParam) {
        var keyword = attributes().getOptionalString(typeParam, "source", FlangName.KEYWORD, FlangName.NAME);
        typeParam.set(TypeParam.KEYWORD, keyword);

        var values = getChildren(typeParam, FlangName.TYPE_PARAM_VALUE);
        typeParam.addChildren(values);
    }

    public void derivedType(DerivedType derivedType) {
        var name = attributes().getString(derivedType, "source", FlangName.NAME);
        derivedType.set(DerivedType.NAME, name);

        var typeParams = getChildren(derivedType, FlangName.TYPE_PARAM_SPEC);
        derivedType.addChildren(typeParams);
    }

    public void intrinsicDeclType(IntrinsicDeclType declType) {
        var intrinsicType = getChild(declType, FlangName.INTRINSIC_TYPE_SPEC);
        declType.addChild(intrinsicType);
    }

    public void derivedDeclType(DerivedDeclType declType) {
        var variantKey = attributes(declType).getVariantKey();
        var kind = DeclTypeKind.valueOf(variantKey.toUpperCase());
        declType.set(DerivedDeclType.KIND, kind);

        var derivedType = getChild(declType, FlangName.DERIVED_TYPE_SPEC);
        declType.addChild(derivedType);
    }

    public void starDeclType(StarDeclType declType) {
        var variantKey = attributes(declType).getVariantKey();
        var kind = DeclTypeKind.valueOf(variantKey.toUpperCase());
        declType.set(DerivedDeclType.KIND, kind);
    }
}
