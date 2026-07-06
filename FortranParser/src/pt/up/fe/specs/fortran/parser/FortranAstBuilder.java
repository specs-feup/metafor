package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.LabelDecl;
import pt.up.fe.specs.fortran.ast.nodes.program.ProgramUnit;
import pt.up.fe.specs.fortran.ast.nodes.utils.LabelRef;
import pt.up.fe.specs.fortran.parser.processors.Nodes;
import pt.up.fe.specs.util.SpecsCheck;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Builds a Fortran AST based on the processed data.
 */
public class FortranAstBuilder {

    private static final String UNUSED_LABEL = "unused_label";

    private final FortranJsonResult data;

    public FortranAstBuilder(FortranJsonResult data) {
        this.data = data;
    }

    public FortranNode build() {

        // Process each node
        var processor = new Nodes(data);
        for (var node : data.fortranNodes().values()) {
            processor.process(node);
        }

        // Apply post-processing at the file level
        postProcess();

        // Return the root node
        var firstNode = data.fortranNodes().get(data.firstNode());

        if (firstNode == null) {
            throw new RuntimeException("No FortranNode for id '" + data.firstNode() + "'");
        }

        return firstNode;
    }

    private void postProcess() {
        // Make connections between LabelRefs and the corresponding LabelDecl
        connectLabelDeclsRefs();
    }

    private void connectLabelDeclsRefs() {

        // If no refs, no connections to be made
        if (data.processorData().getLabelRefs().isEmpty()) {
            return;
        }
        // Create LabelDecl map, based on the execution block each label is
        Map<String, LabelDecl> labelDecls = new HashMap<>();
        for (var labelDecl : data.processorData().getLabelDecls()) {
            // Refs that are not used do not have LabelDecl in the code
            // TODO: Check if this is the correct way of handling this
            var prefix = getProgramUnitIdTry(labelDecl).orElse(UNUSED_LABEL);

            var id = getLabelDeclId(prefix, labelDecl.get(LabelDecl.LABEL));
            labelDecls.put(id, labelDecl);
        }

        // For each LabelRef, replace with corresponding LabelDecl
        for (var labelRef : data.processorData().getLabelRefs()) {
            // Refs that are not used do not have LabelDecl in the code
            // TODO: Check if this is the correct way of handling this
            var prefix = getProgramUnitIdTry(labelRef).orElse(UNUSED_LABEL);

            var id = getLabelDeclId(prefix, labelRef.getLabel());
            var labelDecl = labelDecls.get(id);
            SpecsCheck.checkNotNull(labelDecl, () -> "Expected to find a LabelDecl for '" + id + "', but none was found: " + labelDecls);
            labelRef.set(LabelRef.LABEL_DECL, labelDecl);
        }
    }

    private String getLabelDeclId(String programUnitId, Integer labelId) {
        return programUnitId + "#" + labelId.toString();
    }

    private Optional<String> getProgramUnitIdTry(FortranNode node) {
        return node.getAncestorTry(ProgramUnit.class).map(programUnit -> programUnit.get(FortranNode.ID));
    }

    private String getProgramUnitId(FortranNode node) {
        return getProgramUnitIdTry(node).orElseThrow(() -> new RuntimeException("Node without ProgramUnit, probably is not in tree: " + node));
    }


}
