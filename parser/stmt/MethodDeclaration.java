package parser.stmt;

import java.util.ArrayList;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.methods.UserMethod;
import runtime.values.RuntimeValue;

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
        for (String param : params) {
            if (environment.resolveScope(param) != null) {
                System.err.println("Parameter " + param + " in method declaration for " + name + " is already defined as a variable.");
                System.exit(0);
            }
        }

        return environment.declareVariable(name, new UserMethod(params, body), false);
    }
}
