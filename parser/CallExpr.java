package parser;

import runtime.Environment;
import runtime.RuntimeValue;
import runtime.UserMethod;
import runtime.ValueType;
import runtime.Method;
import runtime.NullValue;

public class CallExpr extends Expr {
    private Expr[] arguments;
    private Expr callerExpr;

    public CallExpr(Expr[] arguments, Expr callerExpr) {
        super(NodeType.CallExpr);
        this.arguments = arguments;
        this.callerExpr = callerExpr;
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue method = callerExpr.evaluate(environment);

        // convert the arguments into values
        RuntimeValue[] argumentValues = new RuntimeValue[arguments.length];
        for (int i = 0; i < arguments.length; i++)
            argumentValues[i] = arguments[i].evaluate(environment);

        if (method.getType() == ValueType.Method) {
            // pass them into the method call
            return ((Method) method).getCall().apply(argumentValues, environment);
        }

        if (method.getType() == ValueType.UserMethod) {
            return ((UserMethod) method).call(argumentValues, environment);
        }

        System.err.println("Unexpected function call on non-function identifier.");
        System.exit(0);
        return new NullValue();
    }
}
