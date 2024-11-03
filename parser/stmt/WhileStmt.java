package parser.stmt;

import java.util.ArrayList;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.ValueType;
import runtime.values.BooleanValue;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;

public class WhileStmt extends Stmt {
    private Expr condition;
    private ArrayList<Stmt> body;
    private Expr incrementExpr;

    public WhileStmt(Expr condition, ArrayList<Stmt> body) {
        super(NodeType.While);
        this.body = new ArrayList<>();
        this.condition = condition;
        this.body = body;
    }

    public WhileStmt(Expr condition, ArrayList<Stmt> body, Expr incrementExpr) {
        super(NodeType.While);
        this.body = new ArrayList<>();
        this.condition = condition;
        this.body = body;
        this.incrementExpr = incrementExpr;
    }

    public ArrayList<Stmt> getBody() {
        return body;
    }

    public String toString() {
        return "{ type: " + super.type + ", body: " + body + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue conditionValue = condition.evaluate(environment);

        if (conditionValue.getType() != ValueType.Boolean) {
            System.err.println("Condition for while statement is not a boolean.");
            System.exit(0);
        }
        
        while (((BooleanValue) conditionValue).getValue()) {
            for (Stmt statement : body) {
                // evaluates every statement
                RuntimeValue eval = statement.evaluate(environment);

                if (eval.getType() == ValueType.Return || eval.getType() == ValueType.Break)
                    return eval;
                if (eval.getType() == ValueType.Continue)
                    break;
            }
            
            // increments if this is a for loop
            if (incrementExpr != null) incrementExpr.evaluate(environment);
            
            // updates condition value
            conditionValue = condition.evaluate(environment);
        }

        return new NullValue();
    }
}
