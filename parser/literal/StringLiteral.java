package parser.literal;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.values.RuntimeValue;
import runtime.values.StringValue;

public class StringLiteral extends Expr {
    private String value;

    public StringLiteral(String value) {
        super(NodeType.StringLiteral);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "{ type: " + super.type + ", value: " + value + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        return new StringValue(value);
    }
}
