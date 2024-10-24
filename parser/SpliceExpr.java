package parser;

import runtime.Environment;
import runtime.NumberValue;
import runtime.RuntimeValue;
import runtime.StringValue;
import runtime.ValueType;

public class SpliceExpr extends Expr {
    private Expr string;
    private Expr leftBound;
    private Expr rightBound;

    public SpliceExpr(Expr string, Expr leftBound, Expr rightBound) {
        super(NodeType.SpliceExpr);
        this.string = string;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue value = string.evaluate(environment);
        
        if (value.getType() != ValueType.String) {
            System.err.println("Cannot splice from a non-string expression. Expression provided: " + string);
            System.exit(0);
        }

        RuntimeValue leftValue = leftBound.evaluate(environment);
        RuntimeValue rightValue = rightBound.evaluate(environment);

        if (leftValue.getType() != ValueType.Number || rightValue.getType() != ValueType.Number) {
            System.err.println("Cannot splice with non-number indices.");
            System.exit(0);
        }

        int left = (int) ((NumberValue) leftValue).getValue();
        int right = (int) ((NumberValue) rightValue).getValue();

        String stringValue = value.valueToString();

        if (left < 0 || right >= stringValue.length()) {
            System.err.println("Index out of bounds while splicing.");
            System.exit(0);
        }

        if (left != ((NumberValue) leftValue).getValue() || right != ((NumberValue) rightValue).getValue()) {
            System.err.println("Cannot splice with non-integer indices.");
            System.exit(0);
        }

        return new StringValue(value.valueToString().substring(left, right));
    }
}
