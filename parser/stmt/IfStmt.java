package parser.stmt;

import java.util.ArrayList;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.ValueType;
import runtime.values.BooleanValue;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;

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

        if (((BooleanValue) conditionValue).getValue()) {
            for (Stmt statement : body) {
                RuntimeValue eval = statement.evaluate(environment);
                if (eval.getType() == ValueType.Return || eval.getType() == ValueType.Break)
                    return eval;
                if (eval.getType() == ValueType.Continue)
                    break;
            }
        } else {
            for (Stmt statement : elseBody) {
                RuntimeValue eval = statement.evaluate(environment);
                if (eval.getType() == ValueType.Return || eval.getType() == ValueType.Break)
                    return eval;
                if (eval.getType() == ValueType.Continue)
                    break;
            }
        }

        return new NullValue();
    }
}
