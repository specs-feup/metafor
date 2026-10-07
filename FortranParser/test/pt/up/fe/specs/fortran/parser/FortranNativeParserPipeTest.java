package pt.up.fe.specs.fortran.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.io.TempDir;
import pt.up.fe.specs.fortran.ast.FortranContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FortranNativeParserPipeTest {

    @Test
    @EnabledOnOs(OS.LINUX)
    void drainsRemainingStdoutWhenProtobufParsingFails(@TempDir Path tempDirectory) throws IOException {
        Path source = Files.writeString(tempDirectory.resolve("input.f90"), "program p\nend program p\n");
        Path plugin = Files.createFile(tempDirectory.resolve("placeholder.so"));
        Path drainedMarker = tempDirectory.resolve("stdout-drained");
        Path fakeFlang = tempDirectory.resolve("fake-flang");
        String script = "#!/bin/sh\n"
                + "printf 'invalid protobuf header'\n"
                + "if ! timeout 3s dd if=/dev/zero bs=1048576 count=8 of=/dev/stdout 2>/dev/null; then\n"
                + "  exit 71\n"
                + "fi\n"
                + "touch '" + drainedMarker + "'\n";
        Files.writeString(fakeFlang, script);
        assertTrue(fakeFlang.toFile().setExecutable(true), "Could not make fake Flang executable");

        RuntimeException parseFailure = assertThrows(RuntimeException.class,
                () -> new FortranNativeParser(new FortranContext(), plugin.toFile(), fakeFlang.toString())
                        .parse(source.toFile()));

        assertTrue(Files.exists(drainedMarker), "The producer could not finish writing stdout");
        assertTrue(causeMessages(parseFailure).contains("invalid protocol magic"),
                () -> "Expected the malformed header error, got: " + causeMessages(parseFailure));
    }

    private static String causeMessages(Throwable throwable) {
        StringBuilder messages = new StringBuilder();
        for (Throwable current = throwable; current != null; current = current.getCause()) {
            messages.append(current.getMessage()).append('\n');
            for (Throwable suppressed : current.getSuppressed()) {
                messages.append(suppressed.getMessage()).append('\n');
            }
        }
        return messages.toString();
    }
}
