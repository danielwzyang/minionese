package parser;

import runtime.Environment;
import runtime.NullValue;
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

    public Expr getObject() {
        return object;
    }

    public Expr getProperty() {
        return property;
    }

    public boolean getComputed() {
        return computed;
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue objectValue = object.evaluate(environment);
        if (objectValue.getType() == ValueType.Object) {
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
                // if it wasn't computed then it has to be an identifier; if it isn't we already
                // handled that error in parsing
                key = ((Identifier) property).getName();
            }

            RuntimeValue propertyRuntimeValue = ((ObjectValue) objectValue).getPropertyValue(key);

            if (propertyRuntimeValue == null) {
                return new NullValue();
            }

            return propertyRuntimeValue;
        }
        else if (objectValue.getType() == ValueType.String) {
            RuntimeValue index = property.evaluate(environment);

            if (index.getType() != ValueType.Number) {
                System.err.println("Expected number for character retrieval from string.");
                System.exit(0);
            }

            return ((StringValue) objectValue).splice(index, environment);

        }
        else {
            System.err.println("Unexpected member expression on non-object value.");
            System.exit(0);
            return new NullValue();
        }
    }
}
 