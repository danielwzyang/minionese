package parser;

import runtime.Environment;
import runtime.RuntimeValue;

public abstract class Expr extends Stmt {
    public Expr() { 
        super(null); 
    }

    public Expr(NodeType kind) {
        super(kind);
    }

    public abstract RuntimeValue evaluate(Environment environment);
}