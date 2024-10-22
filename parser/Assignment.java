package parser;

import runtime.Environment;
import runtime.NullValue;
import runtime.RuntimeValue;

public class Assignment extends Expr {
    private Expr assignedExpr;
    private Expr value;
    
    public Assignment(Expr assignedExpr, Expr value) {
        super(NodeType.Assignment);
        this.assignedExpr = assignedExpr;
        this.value = value;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", assignedExpr: " + assignedExpr + ", value: " + value + " }";
    }

    public Expr getAssignedExpr() {
        return assignedExpr;
    }    

    public Expr getValue() {
        return value;
    }

    public RuntimeValue evaluate(Environment environment) {
        // here we're just handling if the assigned expression is an identifier
        if (assignedExpr.getKind() == NodeType.Identifier) {
            String symbol = ((Identifier) assignedExpr).getSymbol();

            return environment.assignVariable(symbol, value.evaluate(environment));
        } else {
            System.err.println("This expression cannot be assigned: " + assignedExpr);
            System.exit(0);
        }

        return new NullValue();
    }
}