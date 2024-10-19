package parser;
public class BinaryExpr extends Expr {
    Expr left, right;
    String operator;

    public BinaryExpr(Expr left, Expr right) {
        super(NodeType.BinaryExpr);
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", left: " + left + ", right: " + right + " }";
    }
}
