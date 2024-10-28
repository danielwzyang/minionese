package runtime.methods;

import java.util.ArrayList;

import parser.NodeType;
import parser.stmt.Stmt;
import runtime.Environment;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;
import runtime.values.ValueType;

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

        RuntimeValue last = new NullValue();

        for (Stmt statement : body) {
            last = statement.evaluate(innerEnvironment);

            if (last.getType() == ValueType.Return)
                return last;
        }

        return last;
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
 