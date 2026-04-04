import JavaTypes, {JavaClasses} from "@specs-feup/lara/api/lara/util/JavaTypes.js";

// eslint-disable-next-line @typescript-eslint/no-namespace
export namespace FortranJavaClasses {
    /* eslint-disable @typescript-eslint/no-empty-object-type */
    export interface AstFactory extends JavaClasses.JavaClass {}
    /* eslint-enable @typescript-eslint/no-empty-object-type */
}

export default class FortranJavaTypes {
    static get AstFactory() {
        return JavaTypes.getType(
            "pt.up.fe.specs.fortran.weaver.importable.AstFactory"
        ) as FortranJavaClasses.AstFactory;
    }
}