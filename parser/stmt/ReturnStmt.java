package parser.stmt;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.values.ReturnValue;
import runtime.values.RuntimeValue;

public class ReturnStmt extends Stmt {
    private Expr value;

    public ReturnStmt(Expr value) {
        super(NodeType.Return);
        this.value = value;
    }

    public RuntimeValue evaluate(Environment environment) {
        return new ReturnValue(value.evaluate(environment));
    }
}