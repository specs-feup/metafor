package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.FortranNodeFactory;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.parser.FlangAttributes;
import pt.up.fe.specs.fortran.parser.FlangData;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public interface NodeProcessor {
    FortranJsonResult data();

    default FortranNodeFactory factory() {
        return data().context().get(FortranContext.FACTORY);
    }

    default FortranNode getChild(String id) {
        return getNode(data().attributes().getChildId(id));
    }

    default FortranNode getNode(String id) {
        var node = data().fortranNodes().get(id);
        Objects.requireNonNull(node, () -> "Could not find a FortranNode for id '" + id + "'");
        return node;
    }


    default FortranNode getChild(FortranNode node, FlangName name) {
        return getChild(node, name.getString());
    }

    default FortranNode getChild(FortranNode node, String attribute) {
        return getNode(attributes().getChildId(node, attribute));
    }

    default String getChildId(FortranNode node, FlangName name) {
        return attributes().getChildId(node, name.getString());
    }

    default String getChildId(FortranNode node, String attribute) {
        return attributes().getChildId(node, attribute);
    }

    default String getChildId(FortranNode node, Pattern attribute) {
        return attributes().getChildId(node, attribute);
    }

    default FortranNode getChild(FortranNode node, Pattern attribute) {
        return getNode(attributes().getChildId(node, attribute));
    }

    default Optional<String> getChildIdOptional(FortranNode node, String attribute) {
        return attributes(node).has(attribute)
                ? Optional.of(attributes().getChildId(node, attribute))
                : Optional.empty();
    }

    default Optional<FortranNode> getChildOptional(FortranNode node, String attribute) {
        return getChildIdOptional(node, attribute)
                .map(this::getNode);
    }

    default Optional<FortranNode> getChildOptional(FortranNode node, FlangName name) {
        return getChildOptional(node, name.getString());
    }

    default FortranNode getStmtChild(FortranNode node, FlangName name) {
        return getChild(node, name.getStmtAttr());
    }

    default FortranNode getUnlabeledStmtChild(FortranNode node, FlangName name) {
        return getChild(node, name.getUnlabeledStmtAttr());
    }

    default String getVariantChildId(FortranNode node) {
        return attributes().getVariantChildId(node);
    }

    default FortranNode getVariantChild(FortranNode node) {
        return getNode(getVariantChildId(node));
    }

    default boolean hasVariant(FortranNode node) {
        return attributes().getAttrs(node).hasVariant();
    }

    default List<FortranNode> getChildren(FortranNode node, FlangName attribute) {
        return attributes().getChildrenIds(node, attribute).stream()
                .map(this::getNode)
                .toList();
    }

    default List<FortranNode> getChildren(String key, FlangName attribute) {
        return attributes().getChildrenIds(key, attribute).stream()
                .map(this::getNode)
                .toList();
    }

    default List<FortranNode> getChildren(FortranNode node, String attribute) {
        return attributes().getChildrenIds(node, attribute).stream()
                .map(this::getNode)
                .toList();
    }


    /*
        default FlangAttributes getAttrs(FortranNode node) {
            return getAttrs(node.get(FortranNode.ID));
        }

        default FlangAttributes getAttrs(String id) {
            var attrs = data().attributes().get(id);
            Objects.requireNonNull(attrs, () -> "Id '" + id + "' does not have attributes associated: " + data().attributes());
            return attrs;
        }
    */
    /*
        default String getId(Map<String, Object> attrs) {
            return getString(attrs, "id");
        }
    */
    /*
    default String getString(Map<String, Object> attrs, String key) {
        var value = attrs.get(key);
        Objects.requireNonNull(value, () -> "No attribute '" + key + "': " + attrs);
        return value.toString();
    }
*/
    /*
    default String getKind(String id) {
        return FortranJsonParser.getKind(id);
    }

*/


    default FlangData attributes() {
        return data().attributes();
    }

    default FlangAttributes attributes(FortranNode node) {
        return data().attributes().getAttrs(node);
    }


}
