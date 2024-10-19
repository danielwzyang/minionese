package parser;
public class Expr extends Stmt {
    public Expr() { 
        super(null); 
    }

    public Expr(NodeType kind) {
        super(kind);
    }
}