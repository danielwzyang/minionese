package parser.expr;

import parser.NodeType;
import parser.stmt.Stmt;
import runtime.Environment;
import runtime.values.RuntimeValue;

public abstract class Expr extends Stmt {
    public Expr() { 
        super(null); 
    }

    public Expr(NodeType kind) {
        super(kind);
    }

    public abstract RuntimeValue evaluate(Environment environment);
}