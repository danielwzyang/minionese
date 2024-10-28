package runtime;

import java.util.ArrayList;

import parser.NodeType;
import parser.Stmt;

public class UserMethod extends RuntimeValue {
    private String[] params;
    private ArrayList<Stmt> body;
    
    public UserMethod(String[] params, ArrayList<Stmt> body) {
        super(ValueType.UserMethod);
        this.params = params;
        this.body = body;
    }

    public RuntimeValue call(RuntimeValue[] arguments, Environment environment) {
        Environment innerEnvironment = new Environment(environment);

        if (arguments.length != params.length) {
            System.out.println("Expected " + params.length + " arguments but received " + arguments.length + ".");
            System.exit(0);
        }

        for (int i = 0; i < arguments.length; i++) {
            innerEnvironment.declareVariable(params[i], arguments[i], false);
        }

        RuntimeValue last = new NullValue();

        for (Stmt statement : body) {
            last = statement.evaluate(innerEnvironment);
        }

        return last;
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
 