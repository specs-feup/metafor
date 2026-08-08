package pt.up.fe.specs.fortran.ast.nodes.specification.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.construct.SpecConstruct;
import pt.up.fe.specs.fortran.ast.nodes.specification.genericspec.GenericSpec;
import pt.up.fe.specs.fortran.ast.nodes.stmt.interfaces.EndInterfaceStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.interfaces.InterfaceStmt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InterfaceBlock extends SpecConstruct {
    public InterfaceBlock(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<GenericSpec> getGenericSpec() {
        return getInterfaceStmt().getGenericSpec();
    }

    public InterfaceStmt getInterfaceStmt() {
        return getChild(InterfaceStmt.class, 0);
    }

    public List<InterfaceSpecification> getInterfaceSpecifications() {
        return getChildrenOf(InterfaceSpecification.class);
    }

    public EndInterfaceStmt getEndInterfaceStmt() {
        return getChild(EndInterfaceStmt.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var interfaceStmtCode = getInterfaceStmt().getCode();

        var interfaceSpecificationsCode = getInterfaceSpecifications().stream()
                .map(specification -> indent(specification.getCode()) + ln())
                .collect(Collectors.joining(ln()));

        var endInterfaceStmtCode = getEndInterfaceStmt().getCode();

        return interfaceStmtCode + ln() +
                interfaceSpecificationsCode +
                endInterfaceStmtCode;
    }
}
