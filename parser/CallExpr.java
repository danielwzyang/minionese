package parser;

import runtime.Environment;
import runtime.RuntimeValue;
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
        if (method.getType() != ValueType.Method) {
            System.err.println("Unexpected function call on non-function identifier.");
            System.exit(0);
        }

        // convert the arguments into values
        RuntimeValue[] argumentValues = new RuntimeValue[arguments.length];
        for (int i = 0; i < arguments.length; i++) 
            argumentValues[i] = arguments[i].evaluate(environment);

        // pass them into the method call
        return ((Method) method).getCall().apply(argumentValues, environment);
    }
}
