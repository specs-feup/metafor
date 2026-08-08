package pt.up.fe.specs.fortran.ast.nodes.decl.component;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Initialization;
import pt.up.fe.specs.fortran.ast.nodes.specification.coshape.CoarraySpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ComponentArraySpec;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.CharLenSelector;
import pt.up.fe.specs.fortran.ast.nodes.type.lenselector.LenSelector;

import java.util.Collection;
import java.util.Optional;

public class ComponentDecl extends FortranNode {
    public static final DataKey<String> COMPONENT_NAME = KeyFactory.string("component_name");

    public ComponentDecl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getComponentName() {
        return get(COMPONENT_NAME);
    }

    public Optional<ComponentArraySpec> getArraySpec() {
        return getChildOf(ComponentArraySpec.class);
    }

    public Optional<CoarraySpec> getCoarraySpec() {
        return getChildOf(CoarraySpec.class);
    }

    public Optional<CharLenSelector> getLenSelector() {
        return getChildOf(CharLenSelector.class);
    }

    public Optional<Initialization> getInitialization() {
        return getChildOf(Initialization.class);
    }

    @Override
    public String getCode() {
        var name = getComponentName();

        var arraySpecCode = getArraySpec().map(ComponentArraySpec::getCode).orElse("");
        var coarraySpecCode = getCoarraySpec().map(CoarraySpec::getCode).orElse("");
        var lenSelectorCode = getLenSelector().map(LenSelector::getCode).orElse("");
        var initializationCode = getInitialization().map(Initialization::getCode).orElse("");

        return name + arraySpecCode + coarraySpecCode + lenSelectorCode + initializationCode;
    }
}
