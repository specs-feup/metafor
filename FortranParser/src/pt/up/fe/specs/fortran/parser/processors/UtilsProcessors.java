package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.stmt.CommentStmt;
import pt.up.fe.specs.fortran.ast.nodes.utils.*;
import pt.up.fe.specs.fortran.ast.nodes.utils.enums.IoControlSpecKind;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;
import pt.up.fe.specs.util.utilities.PrintOnce;

public class UtilsProcessors extends ANodeProcessor {


    public UtilsProcessors(FortranJsonResult data) {
        super(data);
    }

    public void format(Format format) {
        var childId = getVariantChildId(format);

        if (data().attributes().isIdInteger(childId)) {
            // Create placeholder LabelDecl
            var labelRef = factory().labelRef(factory().labelDecl(Integer.parseInt(childId)));
            format.addChild(labelRef);
            data().processorData().addLabelRef(labelRef);
            return;
        }

        format.addChild(getChild(childId));
    }

    public void star(Star star) {

    }

    public void nameValue(NameValue nameValue) {
        nameValue.set(NameValue.NAME, attributes().getString(nameValue, "source", FlangName.NAME));

        if (attributes(nameValue).has("uint64_t")) {
            nameValue.setOptional(NameValue.VALUE, Integer.parseInt(attributes(nameValue).getString("uint64_t")));
        }
    }

    public void ioUnit(IoUnit ioUnit) {
        var variantKey = attributes(ioUnit).getVariantKey();
        ioUnit.addChild(getChild(ioUnit, variantKey));
    }

    public void ioControlSpec(IoControlSpec ioControlSpec) {
        var childId = attributes(ioControlSpec).getVariantString();

        ioControlSpec.set(
                IoControlSpec.KIND,
                IoControlSpecKind.valueOf(attributes().get(childId).getString("kind").toUpperCase())
        );

        ioControlSpec.addChild(getChild(attributes().get(childId).getString(FlangName.EXPR)));
    }

    public void commentStmt(CommentStmt commentStmt) {
        var content = attributes().getString(commentStmt, "content");
        commentStmt.set(CommentStmt.CONTENT, content);

        var stmtId = attributes().getString(commentStmt, "stmtId");
        var stmtNode = getChild(stmtId);
        var parentNode = stmtNode.getParent();
        PrintOnce.info("Statement with id '" + stmtId + "' has " + (parentNode != null ? "" : "no ") + "parent");
        assert parentNode != null;

        var numComments = (int)commentStmt.getChildren()
                .stream()
                .filter(child -> child instanceof CommentStmt)
                .count();
        parentNode.addChild(numComments, commentStmt);
    }
}
