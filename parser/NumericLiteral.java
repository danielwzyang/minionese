package parser;

import runtime.Environment;
import runtime.NumberValue;
import runtime.RuntimeValue;

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

    public RuntimeValue evaluate(Environment environment) {
        return new NumberValue(value);
    }
}