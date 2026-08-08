package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.decl.*;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.ComponentDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.attr.AccessComponentAttr;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.attr.CodimComponentAttr;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.attr.DimComponentAttr;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.attr.OtherComponentAttr;
import pt.up.fe.specs.fortran.ast.nodes.decl.enums.ComponentAttrKind;
import pt.up.fe.specs.fortran.ast.nodes.specification.*;
import pt.up.fe.specs.fortran.ast.nodes.specification.enums.AccessKind;
import pt.up.fe.specs.fortran.ast.nodes.specification.enums.EmptyFunctionSpecKind;
import pt.up.fe.specs.fortran.ast.nodes.specification.funcspec.DeclTypeFunctionSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.funcspec.EmptyFunctionSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.type.AbstractTypeAttr;
import pt.up.fe.specs.fortran.ast.nodes.specification.type.AccessTypeAttr;
import pt.up.fe.specs.fortran.ast.nodes.specification.type.BindTypeAttr;
import pt.up.fe.specs.fortran.ast.nodes.specification.type.ExtendsTypeAttr;
import pt.up.fe.specs.fortran.ast.nodes.stmt.AccessStmt;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.DeferredTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.ExprTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.StarTypeParamValue;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.specification.enums.GenericSpecKind;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.NameGenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.OpGenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.OtherGenericSpec;
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

    public void starParameter(StarParameter ignoredParameter) {}

    public void exprTypeParamValue(ExprTypeParamValue value) {
        var expr = getChild(value, FlangName.EXPR);
        value.addChild(expr);
    }

    public void starTypeParamValue(StarTypeParamValue ignoredValue) {}

    public void deferredTypeParamValue(DeferredTypeParamValue ignoredValue) {}

    public void namedOperator(NamedOperator namedOperator) {
        var operatorName = attributes().getString(namedOperator, "source", FlangName.NAME);
        namedOperator.set(NamedOperator.OPERATOR_NAME, operatorName);
    }

    public void intrinsicOperator(IntrinsicOperator intrinsicOperator) {
        var operatorKindSource = attributes(intrinsicOperator).getString("value");
        var operatorKind = BinaryOperatorKind.valueOf(operatorKindSource.toUpperCase());
        intrinsicOperator.set(IntrinsicOperator.OPERATOR_KIND, operatorKind);
    }

    public void nameGenericSpec(NameGenericSpec spec) {
        var name = attributes().getString(spec, "source", FlangName.NAME);
        spec.set(NameGenericSpec.NAME, name);
    }

    public void opGenericSpec(OpGenericSpec spec) {
        var definedOperator = getChild(spec, FlangName.DEFINED_OPERATOR);
        spec.addChild(definedOperator);
    }

    public void otherGenericSpec(OtherGenericSpec spec) {
        var variant = attributes(spec).getVariantName();

        var kind = switch (variant) {
            case ASSIGNMENT -> GenericSpecKind.ASSIGNMENT;
            case READ_FORMATTED -> GenericSpecKind.READ_FORMATTED;
            case READ_UNFORMATTED -> GenericSpecKind.READ_UNFORMATTED;
            case WRITE_FORMATTED -> GenericSpecKind.WRITE_FORMATTED;
            case WRITE_UNFORMATTED -> GenericSpecKind.WRITE_UNFORMATTED;
            default -> throw new RuntimeException("Unknown generic spec kind: " + variant);
        };
        spec.set(OtherGenericSpec.KIND, kind);
    }

    public void letterSpec(LetterSpec letterSpec) {
        var firstLetter = attributes(letterSpec).getString("firstLetter").charAt(0);
        letterSpec.set(LetterSpec.FIRST_LETTER, firstLetter);

        var lastLetter = attributes(letterSpec)
                .getOptionalString("lastLetter")
                .map(s -> s.charAt(0));
        letterSpec.set(LetterSpec.LAST_LETTER, lastLetter);
    }

    public void implicitSpec(ImplicitSpec implicitSpec) {
        var declType = getChild(implicitSpec, FlangName.DECLARATION_TYPE_SPEC);
        implicitSpec.addChild(declType);

        var letterSpecs = getChildren(implicitSpec, FlangName.LETTER_SPEC);
        implicitSpec.addChildren(letterSpecs);
    }

    public void declTypeFunctionSpec(DeclTypeFunctionSpec functionSpec) {
        var declType = getChild(functionSpec, FlangName.DECLARATION_TYPE_SPEC);
        functionSpec.addChild(declType);
    }

    public void emptyFunctionSpec(EmptyFunctionSpec functionSpec) {
        var variantKey = attributes(functionSpec).getVariantKey();
        var kind = EmptyFunctionSpecKind.valueOf(variantKey.toUpperCase());
        functionSpec.set(EmptyFunctionSpec.KIND, kind);
    }

    public void abstractTypeAttr(AbstractTypeAttr ignoredAttr) {}

    public void accessTypeAttr(AccessTypeAttr accessTypeAttr) {
        var accessSpec = attributes().getString(accessTypeAttr, "value", FlangName.ACCESS_SPEC, FlangName.KIND);
        var accessKind = AccessKind.valueOf(accessSpec.toUpperCase());
        accessTypeAttr.set(AccessStmt.ACCESS_KIND, accessKind);
    }

    public void bindTypeAttr(BindTypeAttr ignoredAttr) {}

    public void extendsTypeAttr(ExtendsTypeAttr extendsTypeAttr) {
        var parentType = attributes().getString(extendsTypeAttr, "source", FlangName.EXTENDS, FlangName.NAME);
        extendsTypeAttr.set(ExtendsTypeAttr.PARENT_TYPE, parentType);
    }

    public void accessComponentAttr(AccessComponentAttr accessComponentAttr) {
        var accessSpec = attributes().getString(accessComponentAttr, "value", FlangName.ACCESS_SPEC, FlangName.KIND);
        var accessKind = AccessKind.valueOf(accessSpec.toUpperCase());
        accessComponentAttr.set(AccessStmt.ACCESS_KIND, accessKind);
    }

    public void codimComponentAttr(CodimComponentAttr codimComponentAttr) {
        var coarraySpec = getChild(codimComponentAttr, FlangName.COARRAY_SPEC);
        codimComponentAttr.addChild(coarraySpec);
    }

    public void dimComponentAttr(DimComponentAttr dimComponentAttr) {
        var arraySpec = getChild(dimComponentAttr, FlangName.COMPONENT_ARRAY_SPEC);
        dimComponentAttr.addChild(arraySpec);
    }

    public void otherComponentAttr(OtherComponentAttr otherComponentAttr) {
        var variantKey = attributes(otherComponentAttr).getVariantKey();
        var kind = ComponentAttrKind.valueOf(variantKey.toUpperCase());
        otherComponentAttr.set(OtherComponentAttr.KIND, kind);
    }

    public void componentDecl(ComponentDecl componentDecl) {
        var componentName = attributes().getString(componentDecl, "source", FlangName.NAME);
        componentDecl.set(ComponentDecl.COMPONENT_NAME, componentName);

        var arraySpec = getChildOptional(componentDecl, FlangName.COMPONENT_ARRAY_SPEC);
        arraySpec.ifPresent(componentDecl::addChild);

        var coarraySpec = getChildOptional(componentDecl, FlangName.COARRAY_SPEC);
        coarraySpec.ifPresent(componentDecl::addChild);

        var charLenSelector = getChildOptional(componentDecl, FlangName.CHAR_LENGTH);
        charLenSelector.ifPresent(componentDecl::addChild);

        var initialization = getChildOptional(componentDecl, FlangName.INITIALIZATION);
        initialization.ifPresent(componentDecl::addChild);
    }

    public void derivedTypeDef(DerivedTypeDef derivedTypeDef) {
        var derivedTypeStmt = getChild(derivedTypeDef, FlangName.DERIVED_TYPE_STMT);
        derivedTypeDef.addChild(derivedTypeStmt);

//        var typeParamDefStmts = getChildren(derivedTypeDef, FlangName.TYPE_PARAM_DEF_STMT);
//        derivedTypeDef.addChildren(typeParamDefStmts);
//
//        var privateOrSequenceStmts = getChildren(derivedTypeDef, FlangName.PRIVATE_OR_SEQUENCE);
//        derivedTypeDef.addChildren(privateOrSequenceStmts);

        var componentDefStmts = getChildren(derivedTypeDef, FlangName.COMPONENT_DEF_STMT);
        derivedTypeDef.addChildren(componentDefStmts);

//        var typeBoundProcedurePart = getChildOptional(derivedTypeDef, FlangName.TYPE_BOUND_PROCEDURE_PART);
//        typeBoundProcedurePart.ifPresent(derivedTypeDef::addChild);

        var endTypeStmt = getChild(derivedTypeDef, FlangName.END_TYPE_STMT);
        derivedTypeDef.addChild(endTypeStmt);
    }
}
