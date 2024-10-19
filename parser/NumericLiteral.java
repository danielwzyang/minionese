package parser;

public class NumericLiteral extends Expr {
    int value;

    public NumericLiteral(int value) {
        super(NodeType.NumericLiteral);
        this.value = value;
    }
}