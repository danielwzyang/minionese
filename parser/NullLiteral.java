package parser;
public class NullLiteral extends Expr {
    private String value;

    public NullLiteral() {
        super(NodeType.NullLiteral);
        value = "null";
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", value: " + value + " }";
    }
}