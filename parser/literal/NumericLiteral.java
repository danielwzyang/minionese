package parser.literal;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.values.NumberValue;
import runtime.values.RuntimeValue;

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
        return "{ type: " + super.type + ", value: " + value + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        return new NumberValue(value);
    }
}