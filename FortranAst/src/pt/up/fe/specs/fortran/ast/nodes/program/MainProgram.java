package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.END;
import static pt.up.fe.specs.fortran.ast.FortranKeyword.PROGRAM;

/**
 * R1401 main-program
 * <p>
 * [program-stmt] [specification-part] [execution-part] [internal-subprogram-part] end-program-stmt
 * <p>
 * Missing: [specification-part] [execution-part] [internal-subprogram-part]
 */
public class MainProgram extends ProgramUnit {

    // DATAKEYS BEGIN

    public final static DataKey<Optional<String>> NAME = KeyFactory.optional("programName");

    // DATAKEYS END

    public MainProgram(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getName() {
        return get(NAME);
    }

    public Optional<ProgramStmt> getProgramStmt() {
        return getChildOf(ProgramStmt.class);
    }

    public EndProgramStmt getEndProgramStmt() {
        return getChild(EndProgramStmt.class);
    }

    @Override
    public String getCode() {
        var programStmtOpt = getProgramStmt();
        var endProgramStmt = getEndProgramStmt();

        var code = new StringBuilder();

        programStmtOpt.ifPresent(stmt -> code.append(stmt.getCode()).append(ln()));

        code.append(getBodyCode()).append(ln());

        code.append(endProgramStmt.getCode()).append(ln());

        return code.toString();
    }
}
