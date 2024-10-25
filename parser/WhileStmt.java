package parser;

import java.util.ArrayList;

import runtime.BooleanValue;
import runtime.Environment;
import runtime.NullValue;
import runtime.RuntimeValue;
import runtime.ValueType;

public class WhileStmt extends Stmt {
    private Expr condition;
    private ArrayList<Stmt> body;

    public WhileStmt(Expr condition, ArrayList<Stmt> body) {
        super(NodeType.While);
        this.body = new ArrayList<>();
        this.condition = condition;
        this.body = body;
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

        RuntimeValue last = new NullValue();
        
        while (((BooleanValue) conditionValue).getValue()) {
            for (Stmt statement : body) {
                // evaluates every statement
                last = statement.evaluate(environment);
            }
            // updates condition value
            conditionValue = condition.evaluate(environment);
        }

        return last;
    }
}
