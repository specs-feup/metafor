package pt.up.fe.specs.fortran.ast;

public class FortranKeywords {

    private final boolean lowercase;

    public FortranKeywords(boolean lowercase) {
        this.lowercase = lowercase;
    }

    public FortranKeywords() {
        this(false);
    }

    public FortranKeywords(FortranKeywords fortranKeywords) {
        this(fortranKeywords.lowercase);
    }

    public String get(FortranKeyword keyword) {
        return keyword.getKeyword(lowercase);
    }

    public boolean isLowercase() {
        return lowercase;
    }
}
