package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ElseIfStmt extends IfThenStmt {
     public ElseIfStmt(DataStore data, Collection<? extends FortranNode> children) {
         super(data, children);
     }
}
