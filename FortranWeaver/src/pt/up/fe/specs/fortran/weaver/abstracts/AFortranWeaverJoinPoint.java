package pt.up.fe.specs.fortran.weaver.abstracts;

import org.lara.interpreter.weaver.interf.JoinPoint;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.utils.Position;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AProgram;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.treenode.NodeInsertUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Abstract class which can be edited by the developer. This class will not be overwritten.
 *
 * @author Lara Weaver Generator
 */
public abstract class AFortranWeaverJoinPoint extends AJoinPoint {

    /**
     * Compares the two join points based on their node reference of the used compiler/parsing tool.<br>
     * This is the default implementation for comparing two join points. <br>
     * <b>Note for developers:</b> A weaver may override this implementation in the editable abstract join point, so
     * the changes are made for all join points, or override this method in specific join points.
     */
    @Override
    public boolean compareNodes(AJoinPoint aJoinPoint) {
        return this.getNode().equals(aJoinPoint.getNode());
    }

    @Override
    public String getCodeImpl() {
        return getNode().getCode();
    }

    @Override
    public AJoinPoint getParentImpl() {
        return FortranJoinpoints.create(getNode().getParent());
    }

    @Override
    public AProgram getRootImpl() {
        return (AProgram) getWeaverEngine().getRootJp();
    }

    @Override
    public AJoinPoint[] getChildrenArrayImpl() {
        return FortranJoinpoints.create(getNode().getChildren(), AJoinPoint.class);
    }

    @Override
    public AJoinPoint[] getDescendantsArrayImpl() {
        return FortranJoinpoints.create(getNode().getDescendants(), AJoinPoint.class);
    }

    @Override
    public AJoinPoint[] getScopeNodesArrayImpl() {
        throw new NotImplementedException(this);
    }

    @Override
    public Stream<JoinPoint> getJpChildrenStream() {
        return getNode().getChildrenStream()
                .map(node -> FortranJoinpoints.create(node, AJoinPoint.class));
    }

    @Override
    public JoinPoint getJpParent() {
        return getParentImpl();
    }

    @Override
    public AJoinPoint[] insertImpl(String position, JoinPoint joinPoint) {
        var insertedNode = getNode().insert(Position.valueOf(position.toUpperCase()), (FortranNode) joinPoint.getNode());

        return new AJoinPoint[]{FortranJoinpoints.create(insertedNode, AJoinPoint.class)};
    }

    @Override
    public AJoinPoint[] insertImpl(String position, String code) {
        var insertedNode = getNode().insert(Position.valueOf(position.toUpperCase()), code);

        return new AJoinPoint[]{FortranJoinpoints.create(insertedNode, AJoinPoint.class)};
    }

    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        var insertedNode = getNode().insert(Position.AFTER, node.getNode());

        return FortranJoinpoints.create(insertedNode);
    }

    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        var insertedNode = getNode().insert(Position.AFTER, node.getNode());

        return FortranJoinpoints.create(insertedNode);
    }


    public static FortranNode replace(FortranNode target, FortranNode newNode) {
        return NodeInsertUtils.replace(target, newNode);
    }

    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        return FortranJoinpoints.create(replace(getNode(), node.getNode()));
    }

    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint[] node) {
        // Insert nodes after in reverse order, to preserve order of comments and pragmas
        var reverseNodes = Arrays.asList(node);
        Collections.reverse(reverseNodes);

        AJoinPoint topInserted = null;
        for (var nodeToInsert : reverseNodes) {
            topInserted = insertAfterImpl(nodeToInsert);
        }

        // Remove current node from the tree
        detach();

        // Return the first inserted element
        return topInserted;
    }

    @Override
    public AJoinPoint detachImpl() {
        FortranNode node = getNode();

        if (!node.hasParent()) {
            SpecsLogs.msgInfo(
                    "action detach: could not find a parent in joinpoint of type '" + getJoinPointType() + "'");
            return this;
        }

        node.detach();
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AJoinPoint)) {
            return false;
        }

        return getNode().equals(((AJoinPoint) obj).getNode());
    }

    @Override
    public int hashCode() {
        return getNode().hashCode();
    }

    @Override
    public AJoinPoint getAncestorImpl(String type) {
        Objects.requireNonNull(type, () -> "Missing type of ancestor in attribute 'ancestor'");

        if (type.equals("program")) {
            SpecsLogs.warn("Consider using attribute .root, instead of .ancestor('program')");
        }

        FortranNode currentNode = getNode();
        while (currentNode.hasParent()) {
            // Create join point for testing type
            AFortranWeaverJoinPoint parentJp = FortranJoinpoints.create(currentNode.getParent());

            if (parentJp.instanceOf(type)) {
                return parentJp;
            }

            currentNode = parentJp.getNode();
        }

        return null;
    }

    @Override
    public Boolean containsImpl(AJoinPoint jp) {
        FortranNode clavaNode = jp.getNode();

        return getNode().getDescendantsStream()
                .anyMatch(child -> child == clavaNode);
    }

    @Override
    public AJoinPoint getLeftJpImpl() {
        return getNode().getLeft().map(FortranJoinpoints::create).orElse(null);
    }

    @Override
    public AJoinPoint getRightJpImpl() {
        return getNode().getRight().map(FortranJoinpoints::create).orElse(null);
    }

    @Override
    public Integer getIndexOfSelfImpl() {
        return getNode().indexOfSelf();
    }

    @Override
    public AJoinPoint copyImpl() {
        FortranNode copiedNode = getNode().copyShallow();
        return FortranJoinpoints.create(copiedNode);
    }

    @Override
    public AJoinPoint deepCopyImpl() {
        FortranNode copiedNode = getNode().copy();
        return FortranJoinpoints.create(copiedNode);
    }
}
