package pt.up.fe.specs.fortran.parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranAstOptions;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.lang.SpecsPlatforms;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FortranParserTest {

    private static final DataStore DEFAULT_OPTIONS = DataStore.newInstance(FortranAstOptions.STORE_DEFINITION);

    @BeforeAll
    static void setupOnce() {
        SpecsSystem.programStandardInit();
    }

    private static final String BASE_RESOURCE = "fortran/parser/";

    private static void testNative(String resource) {
        testNative(resource, DataStore.newInstance(FortranAstOptions.STORE_DEFINITION));
    }

    private static void testNative(String resource, DataStore fortranOptions) {
        test(resource, (r, c) -> new FortranNativeParser(c).parse(SpecsIo.resourceToStream(r)), fortranOptions);
    }

    private static void testJson(String resource) {
        testJson(resource, DataStore.newInstance(FortranAstOptions.STORE_DEFINITION));
    }

    private static void testJson(String resource, DataStore fortranOptions) {
        test(resource, (r, c) -> FortranJsonParser.parse(new InputStreamReader(SpecsIo.resourceToStream(r), StandardCharsets.UTF_8), c), fortranOptions);
    }

    private static void test(String resource, BiFunction<String, FortranContext, FortranJsonResult> parser, DataStore fortranOptions) {
        // Read json resource
        var resourceName = BASE_RESOURCE + resource;
        if (!SpecsIo.hasResource(resourceName)) {
            fail("Could not find input resource '" + resourceName + "'");
        }

        // Parse
        var context = new FortranContext(fortranOptions);
        var parseResult = parser.apply(resourceName, context);
        var rootNode = new FortranAstBuilder(parseResult).build();
        //System.out.println(parseResult);
        System.out.println("AST: " + rootNode.toTree());
        System.out.println("CODE:\n" + rootNode.getCode());

        var code = rootNode.getCode();

        // Get expected output resource
        var expectedResourceName = BASE_RESOURCE + SpecsIo.removeExtension(resource) + ".expected.f90";
        if (!SpecsIo.hasResource(resourceName)) {
            fail("Could not find expected output resource '" + expectedResourceName + "'. Expected contents:\n" + code);
        }

        // Compare resource contents with code, normalized
        var expectedNormalized = SpecsStrings.normalizeFileContents(SpecsIo.getResource(expectedResourceName), true);
        var codeNormalized = SpecsStrings.normalizeFileContents(code, true);

        assertEquals(expectedNormalized, codeNormalized, "Codes do not match.\nOriginal code:\n" + code);


    }

    @Test
    void testHelloWorld() {
        testJson("hello.json");
    }

    @Test
    void testNativeParser() {
        if (SpecsPlatforms.isLinux()) {
            testNative("hello.f90");
        }

    }

    @Test
    void testDeclarationNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("declaration.f90");
        }
    }


    @Test
    void testDeclaration() {
        testJson("declaration.json");
    }

    @Test
    void testLogicalExpresion() {
        testJson("logical_expression.json");
    }

    @Test
    void testParenExpr() {
        testJson("parenexpr.json");
    }

    @Test
    void testDo() {
        testJson("do.json");
    }

    @Test
    void testBinaryOperator() {
        testJson("binary_operator.json");
    }

    @Test
    void testBinaryOperatorNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("binary_operator.f90");
        }
    }

    @Test
    void testDoConcurrent() {
        testJson("concurrent.json");
    }

    // Conditional statements

    @Test
    void testIfThenNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/if_then.f90");
        }
    }

    @Test
    void testIfThenElseNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/if_then_else.f90");
        }
    }

    @Test
    void testChainedIfNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/chained_if.f90");
        }
    }

    @Test
    void testNamedChainedIfNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/named_chained_if.f90");
        }
    }

    @Test
    void testIfThen() {
        testJson("conditionalstmt/if_then.json");
    }

    @Test
    void testIfThenElse() {
        testJson("conditionalstmt/if_then_else.json");
    }

    @Test
    void testChainedIf() {
        testJson("conditionalstmt/chained_if.json");
    }

    @Test
    void testNamedChainedIf() {
        testJson("conditionalstmt/named_chained_if.json");
    }

    @Test
    void testLogicalIfNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/logical_if.f90");
        }
    }

    @Test
    void testLogicalIf() {
        testJson("conditionalstmt/logical_if.json");
    }

    @Test
    void testArrayAssignment() {
        testJson("arrays/array_declaration.json");
    }

    @Test
    void testArrayAssignmentNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("arrays/array_declaration.f90");
        }
    }

    @Test
    void testArrayImpliedDo() {
        testJson("arrays/array_implied_do.json");
    }

    @Test
    void testArrayImpliedDoNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("arrays/array_implied_do.f90");
        }
    }

    @Test
    void testSelectCaseNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/select_case.f90");
        }
    }

    @Test
    void testSelectCase() {
        testJson("conditionalstmt/select_case.json");
    }

    @Test
    void testSelectCaseListNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/select_case_list.f90");
        }
    }

    @Test
    void testSelectCaseList() {
        testJson("conditionalstmt/select_case_list.json");
    }

    @Test
    void testSelectCaseRangeNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/select_case_range.f90");
        }
    }

    @Test
    void testSelectCaseRange() {
        testJson("conditionalstmt/select_case_range.json");
    }

    @Test
    void testNamedSelectCaseNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("conditionalstmt/named_select_case.f90");
        }
    }

    @Test
    void testNamedSelectCase() {
        testJson("conditionalstmt/named_select_case.json");
    }

    @Test
    void testSubroutine() {
        testJson("subroutine.json");
    }

    @Test
    void testSubroutineNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("subroutine.f90");
        }
    }

    @Test
    void testArrayElementAccess() {
        testJson("arrays/element_access.json");
    }

    @Test
    void testArrayElementAccessNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("arrays/element_access.f90");
        }
    }

    /* commented for now until some details are added to the AST
    @Test
    void test3mm() {
        testJson("polybench/3mm.json");
    }
    */

    @Test
    void testKindSelector() {
        testJson("decl/kind_selector.json");
    }

    @Test
    void testKindSelectorNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("decl/kind_selector.f90");
        }
    }

    @Test
    void testLegacyKindSelector() {
        testJson("decl/legacy_kind_selector.json");
    }

    @Test
    void testLegacyKindSelectorNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("decl/legacy_kind_selector.f90");
        }
    }

    @Test
    void testDirective() {
        testJson("directive.json");
    }

    @Test
    void testDirectiveNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("directive.f90");
        }
    }

    @Test
    void testNoProgramStmt() {
        testJson("program/no_program_stmt.json");
    }

    @Test
    void testNoProgramStmtNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("program/no_program_stmt.f90");
        }
    }

    @Test
    void testNegate() {
        testJson("expr/negate.json");
    }

    @Test
    void testNegateNative() {
        if (SpecsPlatforms.isLinux()) {
            testNative("expr/negate.f90");
        }
    }

    @Test
    void testFujitsu0000_0000() {
        testJson("fujitsu/0000/0000_0000.json");
    }

    @Test
    void testFujitsu0000_0000Native() {
        if (SpecsPlatforms.isLinux()) {
            testNative("fujitsu/0000/0000_0000.f90");
        }
    }

    @Test
    void testFujitsu0000_0001() {
        testJson("fujitsu/0000/0000_0001.json");
    }

    @Test
    void testFujitsu0000_0001Native() {
        if (SpecsPlatforms.isLinux()) {
            testNative("fujitsu/0000/0000_0001.f90");
        }
    }
}
