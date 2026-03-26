package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.utils.Format;
import pt.up.fe.specs.fortran.ast.nodes.utils.NameValue;
import pt.up.fe.specs.fortran.ast.nodes.utils.Star;
import pt.up.fe.specs.fortran.parser.FlangData;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class UtilsProcessors extends ANodeProcessor {


    public UtilsProcessors(FortranJsonResult data) {
        super(data);
    }

    public void format(Format format) {
        var variantKey = attributes(format).getString(FlangData.VARIANT_IDENTIFIER_KEY);
        var childId = getChildId(format, variantKey);

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
}
