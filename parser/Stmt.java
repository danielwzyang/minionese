package parser;
public class Stmt {
    protected NodeType kind;

    public Stmt(NodeType kind) {
        this.kind = kind;
    }

    public NodeType getKind() {
        return kind;
    }

    public String toString() {
        return "" + kind;
    }
}