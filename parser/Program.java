package parser;
import java.util.Vector;

public class Program extends Stmt {
    private Vector<Stmt> body;

    public Program() {
        super(NodeType.Program);
        this.body = new Vector<>();
    }

    public Vector<Stmt> getBody() {
        return body;
    }

    public void bodyPush(Stmt statement) {
        body.add(statement);
    }
}