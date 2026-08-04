package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.FortranDecl;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AFortranDecl;

public class FFortranDecl extends AFortranDecl {

    public final FortranDecl fortranDecl;

    public FFortranDecl(FortranDecl fortranDecl) {
        this.fortranDecl = fortranDecl;
    }

    @Override
    public FortranNode getNode() {
        return fortranDecl;
    }
}
