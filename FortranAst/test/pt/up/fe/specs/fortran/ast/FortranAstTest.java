package pt.up.fe.specs.fortran.ast;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FortranAstTest {

    private FortranNodeFactory factory;

    @BeforeAll
    static void setupOnce() {
        SpecsSystem.programStandardInit();
    }


    @BeforeEach
    void setUp() {
        factory = new FortranContext().get(FortranContext.FACTORY);
    }

    private static final String BASE_RESOURCE = "fortran/ast/";

    private static void test(String expected, FortranNode node) {
        // Generate code
        var code = node.getCode();

        var resourceName = BASE_RESOURCE + expected;
        if (!SpecsIo.hasResource(resourceName)) {
            System.out.println("CODE:\n" + code);
            fail("Could not find resource '" + resourceName + "'. Expected code:\n\n" + code);
        }

        // Compare resource contents with code, normalized
        var expectedNormalized = SpecsStrings.normalizeFileContents(SpecsIo.getResource(resourceName), true);
        var codeNormalized = SpecsStrings.normalizeFileContents(code, true);
        System.out.println("CODE:\n" + code);
        assertEquals(expectedNormalized, codeNormalized, "Codes do not match.\nOriginal code:\n" + code);
    }

    @Test
    void testMainProgram() {
        // Build AST
        var program = factory.fortranFile(List.of(factory.mainProgram("hello", List.of())));
        test("mainProgram.f90", program);
    }

    @Test
    void testHelloWorld() {
        // Build AST
        var print = factory.printStmt(factory.format(factory.star()), factory.stringLiteral("Hello, World!"));
        var program = factory.fortranFile(List.of(factory.mainProgram("hello", List.of(print))));
        test("hello.f90", program);
    }

    @Test
    void testDoConstructInitializesStatementMetadata() {
        var control = factory.rangeLoopControl(
                factory.dataRef("i"),
                factory.intLiteral(1),
                factory.intLiteral(4)
        );

        var code = factory.doConstruct(control).getCode();

        assertTrue(code.contains("DO i = 1, 4"));
        assertTrue(code.contains("END DO"));
    }


}
