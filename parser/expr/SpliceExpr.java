package parser.expr;

import parser.NodeType;
import runtime.Environment;
import runtime.values.ArrayValue;
import runtime.values.NullValue;
import runtime.values.NumberValue;
import runtime.values.RuntimeValue;
import runtime.values.StringValue;
import runtime.values.ValueType;

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

        if (value.getType() == ValueType.String) {
            StringValue stringValue = (StringValue) value;

            RuntimeValue left = leftBound.evaluate(environment);
            RuntimeValue right = rightBound != null ? rightBound.evaluate(environment)
                    : new NumberValue(stringValue.getValue().length());

            return stringValue.splice(left, right, environment);
        } else if (value.getType() == ValueType.Array) {
            ArrayValue arrayValue = (ArrayValue) value;

            RuntimeValue left = leftBound.evaluate(environment);
            RuntimeValue right = rightBound != null ? rightBound.evaluate(environment)
                    : new NumberValue(arrayValue.length());

            return arrayValue.splice(left, right, environment);
        } else {
            System.err.println("Unexpected member expression on non-object value.");
            System.exit(0);
            return new NullValue();
        }
    }
}
