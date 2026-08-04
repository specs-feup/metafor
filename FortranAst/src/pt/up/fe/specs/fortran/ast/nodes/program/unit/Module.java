package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Specification;

import java.util.Collection;
import java.util.Optional;

public class Module extends ProgramUnit {
    public Module(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return getModuleStmt().getModuleName();
    }

    public ModuleStmt getModuleStmt() {
        return getChild(ModuleStmt.class, 0);
    }

    public Specification getSpecification() {
        return getChild(Specification.class, 1);
    }

    public Optional<ModuleSubprogramPart> getSubprogramPart() {
        return getChildTry(ModuleSubprogramPart.class, 2);
    }

    public EndModuleStmt getEndModuleStmt() {
        return getChild(EndModuleStmt.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var moduleStmtCode = getModuleStmt().getCode();
        var specificationCode = getSpecification().getCode();
        var subprogramPartCode = getSubprogramPart()
                .map(part -> ln() + part.getCode())
                .orElse("");
        var endModuleStmtCode = getEndModuleStmt().getCode();

        return moduleStmtCode + ln()
                + indent(specificationCode) + subprogramPartCode +
                ln() + endModuleStmtCode;
    }
}
