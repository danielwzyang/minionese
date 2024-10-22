package parser;

import runtime.Environment;
import runtime.NullValue;
import runtime.RuntimeValue;

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
        return "{ kind: " + super.kind + ", key: " + key + ", value: " + value + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        return new NullValue();
    }
}
