package runtime.methods;

import java.util.ArrayList;

import parser.stmt.Stmt;
import runtime.Environment;
import runtime.ValueType;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;

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
            if (innerEnvironment.resolveScope(params[i]) != null) {
                System.err.println("Parameter " + params[i] + " is already defined as a variable.");
                System.exit(0);
            }
            innerEnvironment.declareVariable(params[i], arguments[i], false);
        }

        for (Stmt statement : body) {
            RuntimeValue eval = statement.evaluate(innerEnvironment);

            if (eval.getType() == ValueType.Return)
                return eval;
        }

        return new NullValue();
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
 