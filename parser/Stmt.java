package parser;

import runtime.Environment;
import runtime.RuntimeValue;

public abstract class Stmt {
    protected NodeType type;

    public Stmt(NodeType type) {
        this.type = type;
    }

    public NodeType getType() {
        return type;
    }

    public String toString() {
        return "" + type;
    }

    public abstract RuntimeValue evaluate(Environment environment);
}