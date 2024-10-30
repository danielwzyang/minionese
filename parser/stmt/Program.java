package parser.stmt;

import java.util.ArrayList;

import parser.NodeType;
import runtime.Environment;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;

public class Program extends Stmt {
    private ArrayList<Stmt> body;

    public Program() {
        super(NodeType.Program);
        this.body = new ArrayList<>();
    }

    public ArrayList<Stmt> getBody() {
        return body;
    }

    public void bodyPush(Stmt statement) {
        body.add(statement);
    }

    public String toString() {
        return "{ type: " + super.type + ", body: " + body + " }";
    }

    public RuntimeValue evaluate(Environment globalEnvironment) {
        // this is what's called when we run the code since we have to run the program through it
        // here we keep track of the last evaluated statement
        RuntimeValue last = new NullValue();

        for (Stmt statement : body) {
            // goes through every statement and evaluates it
            last = statement.evaluate(globalEnvironment);
        }

        return last;
    }
}