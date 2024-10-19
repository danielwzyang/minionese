package parser;
public class NumericLiteral extends Expr {
    double value;

    public NumericLiteral(double value) {
        super(NodeType.NumericLiteral);
        this.value = value;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", value: " + value + " }";
    }
}