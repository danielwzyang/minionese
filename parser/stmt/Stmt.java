package parser.stmt;

import parser.NodeType;
import runtime.Environment;
import runtime.values.RuntimeValue;

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