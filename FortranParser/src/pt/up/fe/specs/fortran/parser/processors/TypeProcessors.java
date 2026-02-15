package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.type.IntegerType;
import pt.up.fe.specs.fortran.ast.nodes.type.LogicalType;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class TypeProcessors extends ANodeProcessor {


    public TypeProcessors(FortranJsonResult data) {
        super(data);
    }

    public void integerType(IntegerType integerType) {

    }

    public void logicalType(LogicalType logicalType) {

    }


}
