package parser;

import java.util.ArrayList;

import runtime.Environment;
import runtime.RuntimeValue;
import runtime.UserMethod;

public class MethodDeclaration extends Expr {
    private String name;
    private String[] params;
    private ArrayList<Stmt> body;

    public MethodDeclaration(String name, String[] params, ArrayList<Stmt> body) {
        super(NodeType.MethodDeclaration);
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public String[] getParams() {
        return params;
    }

    public RuntimeValue evaluate(Environment environment) {
        return environment.declareVariable(name, new UserMethod(params, body), false);
    }
}
