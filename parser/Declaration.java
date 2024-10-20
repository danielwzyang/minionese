package parser;

import java.util.Vector;

public class Declaration extends Stmt {
    private String identifier;
    private Expr value;
    private boolean isFinal;

    public Declaration(String identifier, boolean isFinal) {
        super(NodeType.Declaration);
        this.identifier = identifier;
        this.isFinal = isFinal;
    }

    public Declaration(String identifier, boolean isFinal, Expr value) {
        super(NodeType.Declaration);
        this.identifier = identifier;
        this.isFinal = isFinal;
        this.value = value;
    }

    public Expr getValue() {
        return value;
    }

    public void setValue(Expr value) {
        this.value = value;
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", identifier: " + identifier + ", value: " + value + ", isFinal: " + isFinal + " }";
    }
}