package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.CompilerDirective;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ACompilerDirective;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExpr;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ANameValue;

public class FCompilerDirective extends ACompilerDirective {

    private final CompilerDirective compilerDirective;

    public FCompilerDirective(CompilerDirective compilerDirective) {
        super(new FExecutableStatement(compilerDirective));
        this.compilerDirective = compilerDirective;
    }

    @Override
    public String getDirectiveStringImpl() {
        return compilerDirective.getDirectiveString();
    }

    @Override
    public ANameValue[] getPairsArrayImpl() {
        return (ANameValue[]) compilerDirective.getPairs()
                .stream()
                .map(FortranJoinpoints::create)
                .toArray();
    }

    @Override
    public FortranNode getNode() {
        return compilerDirective;
    }
}
