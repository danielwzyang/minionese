package parser;

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
}