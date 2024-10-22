package parser;

import runtime.Environment;
import runtime.RuntimeValue;
import runtime.StringValue;

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
