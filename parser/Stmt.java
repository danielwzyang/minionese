package parser;

import runtime.Environment;
import runtime.RuntimeValue;

public abstract class Stmt {
    protected NodeType kind;

    public Stmt(NodeType kind) {
        this.kind = kind;
    }

    public NodeType getKind() {
        return kind;
    }

    public String toString() {
        return "" + kind;
    }

    public abstract RuntimeValue evaluate(Environment environment);
}