package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Subroutine extends ProgramUnit {

    public final static DataKey<String> SUBROUTINE_NAME = KeyFactory.string("programName");

    public Subroutine(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
