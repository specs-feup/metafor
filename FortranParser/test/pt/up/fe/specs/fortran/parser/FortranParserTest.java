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
        //System.out.println("CODE:\n" + rootNode.getCode());

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
    void testBinaryOperator() {
        testJson("binary_operator.json");
    }
}
