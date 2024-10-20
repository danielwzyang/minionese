package parser;
public class NullLiteral extends Expr {
    String value;

    public NullLiteral() {
        super(NodeType.NullLiteral);
        value = "null";
    }

    public String toString() {
        return "{ kind: " + super.kind + ", value: " + value + " }";
    }
}