package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class ProgramStmt extends Stmt {
    public final static DataKey<String> PROGRAM_NAME = KeyFactory.string("program_name");

    public ProgramStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getProgramName() {
        return get(PROGRAM_NAME);
    }

    @Override
    public String getStmtCode() {
        return keyword(FortranKeyword.PROGRAM) + " " + getProgramName();
    }
}
