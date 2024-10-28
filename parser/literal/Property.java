package parser.literal;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;

public class Property extends Expr {
    private String key;
    private Expr value;

    public Property(String key) {
        super(NodeType.Property);
        this.key = key;
    }

    public Property(String key, Expr value) {
        super(NodeType.Property);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Expr getValue() {
        return value;
    }

    public String toString() {
        return "{ type: " + super.type + ", key: " + key + ", value: " + value + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        return new NullValue();
    }
}
