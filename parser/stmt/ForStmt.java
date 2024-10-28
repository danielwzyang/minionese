package parser.stmt;

import java.util.ArrayList;

import parser.NodeType;
import parser.expr.BinaryExpr;
import parser.expr.Expr;
import parser.expr.UnaryExpr;
import parser.literal.Identifier;
import runtime.Environment;
import runtime.values.NumberValue;
import runtime.values.RuntimeValue;
import runtime.values.ValueType;

public class ForStmt extends Stmt {
    private Identifier identifier;
    private Expr left;
    private Expr right;
    private ArrayList<Stmt> body;

    public ForStmt(Identifier identifier, Expr left, Expr right, ArrayList<Stmt> body) {
        super(NodeType.For);
        this.identifier = identifier;
        this.left = left;
        this.right = right;
        this.body = body;
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue leftValue = left.evaluate(environment);
        RuntimeValue rightValue = right.evaluate(environment);

        if (leftValue.getType() != ValueType.Number || rightValue.getType() != ValueType.Number) {
            System.err.println("Numbers expected for bounds of for loop.");
            System.exit(0);
        }

        double leftBound = ((NumberValue) leftValue).getValue();
        double rightBound = ((NumberValue) rightValue).getValue();

        if ((int) leftBound != leftBound || (int) rightBound != rightBound) {
            System.err.println("Integers expected for bounds of for loop.");
            System.exit(0);
        }

        String comparison = leftBound < rightBound ? "<" : ">";
        String increment = leftBound < rightBound ? "++" : "--";

        boolean identifierExists = environment.getVariables().get(identifier.getName()) != null;
        
        // if the variable is already declared then it gets assigned
        if (identifierExists) 
            environment.assignVariable(identifier.getName(), leftValue);
        // if the variable isn't declared yet
        else
            environment.declareVariable(identifier.getName(), leftValue, false);

        Expr condition = new BinaryExpr(identifier, right, comparison);
        body.add(new UnaryExpr(increment, identifier));
        WhileStmt whileStmt = new WhileStmt(condition, body);

        return whileStmt.evaluate(environment);
    }
}
