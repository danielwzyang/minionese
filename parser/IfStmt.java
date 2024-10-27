package parser;

import java.util.ArrayList;

import runtime.BooleanValue;
import runtime.Environment;
import runtime.NullValue;
import runtime.RuntimeValue;
import runtime.ValueType;

public class IfStmt extends Stmt {
    private Expr condition;
    private ArrayList<Stmt> body;
    private ArrayList<Stmt> elseBody;

    public IfStmt(Expr condition, ArrayList<Stmt> body, ArrayList<Stmt> elseBody) {
        super(NodeType.If);
        this.body = new ArrayList<>();
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
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
            System.err.println("Condition for if statement is not a boolean.");
            System.exit(0);
        }

        RuntimeValue last = new NullValue();
        
        if (((BooleanValue) conditionValue).getValue()) {
            for (Stmt statement : body) {
                last = statement.evaluate(environment);
            }
        } else {
            for (Stmt statement : elseBody) {
                last = statement.evaluate(environment);
            }
        }

        return last;
    }
}
