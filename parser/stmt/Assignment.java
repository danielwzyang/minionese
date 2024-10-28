package parser.stmt;

import parser.NodeType;
import parser.expr.Expr;
import parser.expr.MemberExpr;
import parser.literal.Identifier;
import parser.literal.StringLiteral;
import runtime.Environment;
import runtime.values.NullValue;
import runtime.values.ObjectValue;
import runtime.values.RuntimeValue;
import runtime.values.ValueType;

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
            String symbol = ((Identifier) assignedExpr).getName();
            return environment.assignVariable(symbol, value.evaluate(environment));
        }
        // handling if the expression is an object property
        else if (assignedExpr.getType() == NodeType.MemberExpr) {
            // gets the object that contains the property
            RuntimeValue objectValue = ((MemberExpr) assignedExpr).getObject().evaluate(environment);

            if (objectValue.getType() != ValueType.Object) {
                System.err.println("Cannot assign property to non-object value. Attemping to assign property to " + objectValue.getType() + ".");
                System.exit(0);
            }

            // gets the name of the property; if the property is a string then its the string value, if it's an identifier then its the name
            Expr propertyExpr = ((MemberExpr) assignedExpr).getProperty();
            String propertyValue;
            if (propertyExpr.getType() == NodeType.Identifier) propertyValue = ((Identifier) propertyExpr).getName();
            else propertyValue = ((StringLiteral) propertyExpr).getValue();

            return ((ObjectValue) objectValue).addProperty(propertyValue, value.evaluate(environment));
        }
        else {
            System.err.println("This expression cannot be assigned: " + assignedExpr);
            System.exit(0);
        }

        return new NullValue();
    }
}