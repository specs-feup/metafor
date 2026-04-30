package pt.up.fe.specs.fortran.ast.utils;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.util.SpecsLogs;

public class Inserts {

    private final FortranNode baseNode;

    public Inserts(FortranNode baseNode) {
        this.baseNode = baseNode;
    }

    /**
     * Creates a new literal node that is compatible for insertion next or instead of the base node.
     *
     * @param code
     * @return
     */
    public FortranNode newLiteralNode(String code) {
        var parent = baseNode.getParent();

        // If parent of base is an execution, use a literalExecutionStmt
        if (parent instanceof Execution) {
            return baseNode.getFactory().literalExecutionStmt(code);
        }

        SpecsLogs.warn("Conversion from code to literal node not tested for base nodes inside a " + parent.getClass());
        // By default, transforms code into a literal statement.
        // This might need to evolve in the future, depending on where the code is inserted
        return baseNode.getFactory().literalExecutionStmt(code);
    }
}
