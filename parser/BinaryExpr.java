package parser;

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
        return "{ kind: " + super.kind + ", left: " + left + ", right: " + right + ", operator: " + operator + " }";
    }
}
