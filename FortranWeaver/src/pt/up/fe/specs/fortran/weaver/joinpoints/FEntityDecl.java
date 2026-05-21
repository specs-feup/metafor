package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AEntityDecl;

public class FEntityDecl extends AEntityDecl {

    public final EntityDecl entityDecl;

    public FEntityDecl(EntityDecl entityDecl) {
        super(new FFortranDecl(entityDecl));
        this.entityDecl = entityDecl;
    }

    @Override
    public String getNameImpl() {
        return entityDecl.get(EntityDecl.NAME);
    }

    @Override
    public FortranNode getNode() {
        return entityDecl;
    }
}
