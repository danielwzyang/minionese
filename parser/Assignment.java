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
        return "{ type: " + super.type + ", assignedExpr: " + assignedExpr + ", value: " + value + " }";
    }

    public Expr getAssignedExpr() {
        return assignedExpr;
    }    

    public Expr getValue() {
        return value;
    }

    public RuntimeValue evaluate(Environment environment) {
        // here we're just handling if the assigned expression is an identifier
        if (assignedExpr.getType() == NodeType.Identifier) {
            String symbol = ((Identifier) assignedExpr).getSymbol();

            return environment.assignVariable(symbol, value.evaluate(environment));
        }
        // handling if the expression is an object property
        else if (assignedExpr.getType() == NodeType.MemberExpr) {
            // idea: get the identifier from the object nad use assignvariable and just make a new objectvalue
            // use recursive to go through the nested memberexprs
            return new NullValue();
        }
        else {
            System.err.println("This expression cannot be assigned: " + assignedExpr);
            System.exit(0);
        }

        return new NullValue();
    }
}