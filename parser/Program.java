package parser;

public class Program extends Stmt {
    private Stmt[] body;

    public Program(Stmt[] body) {
        super(NodeType.Program);
        this.body = body;
    }

    public Stmt[] getBody() {
        return body;
    }
}