package parser;

import runtime.Environment;
import runtime.ObjectValue;
import runtime.RuntimeValue;
import runtime.StringValue;
import runtime.ValueType;

public class MemberExpr extends Expr {
    private Expr object;
    private Expr property;
    private boolean computed;

    public MemberExpr(Expr object, Expr property, boolean computed) {
        super(NodeType.MemberExpr);
        this.object = object;
        this.property = property;
        this.computed = computed;
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue objectValue = object.evaluate(environment);
        if (objectValue.getType() != ValueType.Object) {
            System.err.println("Unexpected member expression on non-object identifier.");
            System.exit(0);
        }

        String key;
        if (computed) {
            // if the member call expression was computed then it has to be a string
            RuntimeValue propertyValue = property.evaluate(environment);

            if (propertyValue.getType() != ValueType.String) {
                System.err.println("Expected computed expression to be a string for member call.");
                System.exit(0);
            }

            key = ((StringValue) propertyValue).getValue();
        } else {
            // if it wasn't computed then it has to be an identifier; if it isn't we already handled that error in parsing
            key = ((Identifier) property).getSymbol();
        }

        return ((ObjectValue) objectValue).getPropertyValue(key);
    }
}
