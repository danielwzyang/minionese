package parser;
public class NumericLiteral extends Expr {
    private double value;

    public NumericLiteral(double value) {
        super(NodeType.NumericLiteral);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", value: " + value + " }";
    }
}