package pt.up.fe.specs.fortran.weaver.abstracts;

import org.lara.interpreter.weaver.interf.JoinPoint;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.utils.Position;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AProgram;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

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
    public JoinPoint[] insertImpl(String position, JoinPoint joinPoint) {
        var insertedNode = getNode().insert(Position.valueOf(position.toUpperCase()), (FortranNode) joinPoint.getNode());

        return new AJoinPoint[]{FortranJoinpoints.create(insertedNode, AJoinPoint.class)};
    }

    @Override
    public JoinPoint[] insertImpl(String position, String code) {
        var insertedNode = getNode().insert(Position.valueOf(position.toUpperCase()), code);

        return new AJoinPoint[]{FortranJoinpoints.create(insertedNode, AJoinPoint.class)};
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
}
