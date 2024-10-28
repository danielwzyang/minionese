package parser.expr;

import parser.NodeType;
import runtime.Environment;
import runtime.values.RuntimeValue;

public class BinaryExpr extends Expr {
    private Expr left, right;
    private String operator;

    public BinaryExpr(Expr left, Expr right, String operator) {
        super(NodeType.BinaryExpr);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }

    public String toString() {
        return "{ type: " + super.type + ", left: " + left + ", right: " + right + ", operator: " + operator + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        // if the node is a binary expression then we can call evaluate for both the left and right side
        RuntimeValue left = this.left.evaluate(environment);
        RuntimeValue right = this.right.evaluate(environment);

        return left.operate(right, operator);
    }
}
