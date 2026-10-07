package pt.up.fe.specs.fortran.parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.function.Executable;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/** End-to-end comparison of the JSON and experimental protobuf native dumpers. */
class FortranNativeProtobufParityTest {

    private static final String BASE_RESOURCE = "fortran/parser/";
    private static final List<String> NATIVE_FIXTURES = List.of(
            "do.f90",
            "hello.f90",
            "declaration.f90",
            "binary_operator.f90",
            "conditionalstmt/if_then.f90",
            "conditionalstmt/if_then_else.f90",
            "conditionalstmt/chained_if.f90",
            "conditionalstmt/named_chained_if.f90",
            "conditionalstmt/logical_if.f90",
            "arrays/array_declaration.f90",
            "arrays/array_implied_do.f90",
            "conditionalstmt/select_case.f90",
            "conditionalstmt/select_case_list.f90",
            "conditionalstmt/select_case_range.f90",
            "conditionalstmt/named_select_case.f90",
            "subroutine.f90",
            "arrays/element_access.f90",
            "omp/omp_basic.f90",
            "omp/omp_do.f90",
            "omp/omp_clause.f90",
            "omp/omp_reduction.f90",
            "polybench/3mm.f90",
            "decl/kind_selector.f90",
            "decl/legacy_kind_selector.f90",
            "directive.f90",
            "parameter.f90",
            "program/no_program_stmt.f90",
            "expr/negate.f90",
            "expr/parentheses.f90",
            "expr/unary_plus.f90",
            "expr/not.f90",
            "stmt/goto.f90",
            "stmt/stop.f90",
            "stmt/data_stmt.f90",
            "expr/subscript_triplet.f90",
            "stmt/common_stmt.f90",
            "comment.f90",
            "fujitsu/0000/0000_0000.f90",
            "fujitsu/0000/0000_0002.f90",
            "fujitsu/0000/0000_0003.f90",
            "fujitsu/0000/0000_0004.f90",
            "fujitsu/0000/0000_0007.f90",
            "fujitsu/0000/0000_0019.f90",
            "fujitsu/0000/0000_0023.f90",
            "fujitsu/0000/0000_0024.f90",
            "fujitsu/0000/0000_0029.f90",
            "fujitsu/0000/0000_0030.f90",
            "fujitsu/0000/0000_0033.f90");

    @BeforeAll
    static void setupOnce() {
        SpecsSystem.programStandardInit();
    }

    @TestFactory
    @EnabledIfEnvironmentVariable(named = "FLANG_NATIVE_PARITY", matches = "1")
    Stream<DynamicTest> jsonAndProtobufMatchGeneratedFortranOnNativeFixtures() {
        File jsonPlugin = requiredPlugin("FLANG_JSON_PLUGIN");
        File protobufPlugin = requiredPlugin("FLANG_PROTOBUF_PLUGIN");
        String flangCommand = requiredEnvironmentVariable("FLANG_EXECUTABLE");

        return NATIVE_FIXTURES.stream().map(fixture -> DynamicTest.dynamicTest(fixture, () -> {
            String sourceResource = BASE_RESOURCE + fixture;
            String expectedResource = BASE_RESOURCE
                    + fixture.substring(0, fixture.lastIndexOf('.')) + ".expected.f90";
            assertTrue(SpecsIo.hasResource(sourceResource), "Missing source fixture: " + sourceResource);

            GeneratedCode json = parseAndGenerate(sourceResource, jsonPlugin, flangCommand, "dump-ast");
            GeneratedCode protobuf = parseAndGenerate(sourceResource, protobufPlugin, flangCommand,
                    "dump-ast-protobuf");

            List<Executable> checks = new ArrayList<>();
            checks.add(() -> assertGenerated("JSON generation", fixture, json));
            if (SpecsIo.hasResource(expectedResource)) {
                String expectedCode = SpecsStrings.normalizeFileContents(SpecsIo.getResource(expectedResource), true);
                checks.add(() -> {
                    if (json.code() != null) {
                        assertEquals(expectedCode, json.code(), "JSON snapshot baseline differs for " + fixture);
                    }
                });
            }
            checks.add(() -> assertGenerated("Protobuf migration", fixture, protobuf));
            checks.add(() -> {
                if (json.code() != null && protobuf.code() != null) {
                    assertEquals(json.code(), protobuf.code(),
                            "Protobuf migration generated code differs from JSON for " + fixture);
                }
            });

            assertAll("Native output checks for " + fixture, checks);
        }));
    }

    private static GeneratedCode parseAndGenerate(String sourceResource, File plugin, String flangCommand,
                                                  String action) {
        try {
            var context = new FortranContext();
            var result = new FortranNativeParser(context, plugin, flangCommand, action)
                    .parse(SpecsIo.resourceToStream(sourceResource));
            String code = new FortranAstBuilder(result).build().getCode();
            return new GeneratedCode(SpecsStrings.normalizeFileContents(code, true), null);
        } catch (RuntimeException failure) {
            return new GeneratedCode(null, failure);
        }
    }

    private static void assertGenerated(String comparison, String fixture, GeneratedCode generated) {
        if (generated.failure() != null) {
            fail(comparison + " failed for " + fixture, generated.failure());
        }
    }

    private static File requiredPlugin(String environmentVariable) {
        File plugin = new File(requiredEnvironmentVariable(environmentVariable));
        assertTrue(plugin.isFile(), environmentVariable + " must point to a plugin file: " + plugin);
        return plugin;
    }

    private static String requiredEnvironmentVariable(String environmentVariable) {
        String value = System.getenv(environmentVariable);
        assertTrue(value != null && !value.isBlank(), environmentVariable + " must be set");
        return value;
    }

    private record GeneratedCode(String code, RuntimeException failure) {
    }
}
