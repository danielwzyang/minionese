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

    public SpliceExpr(Expr string, Expr leftBound) {
        super(NodeType.SpliceExpr);
        this.string = string;
        this.leftBound = leftBound;
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue value = string.evaluate(environment);

        if (value.getType() != ValueType.String) {
            System.err.println("Cannot splice from a non-string expression. Expression provided: " + string);
            System.exit(0);
        }

        StringValue stringValue = (StringValue) value;

        RuntimeValue left = leftBound.evaluate(environment);
        RuntimeValue right = rightBound != null ? rightBound.evaluate(environment)
                : new NumberValue(stringValue.getValue().length());

        return stringValue.splice(left, right, environment);
    }
}
