package parser;

public class StringLiteral extends Expr {
    private String value;

    public StringLiteral(String value) {
        super(NodeType.StringLiteral);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", value: " + value + " }";
    }
}
