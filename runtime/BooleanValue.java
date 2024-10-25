package runtime;

public class BooleanValue extends RuntimeValue {
    private boolean value;

    public BooleanValue() {
        super(ValueType.Boolean);
        value = false;
    }

    public BooleanValue(boolean value) {
        super(ValueType.Boolean);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public String toString() {
        return "" + value;
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        switch (right.getType()) {
            case ValueType.Boolean:
                Boolean rightBoolean = ((BooleanValue) right).getValue();
                switch (operator) {
                    case "&":
                        return new BooleanValue(value && rightBoolean);
                    case "|":
                        return new BooleanValue(value || rightBoolean);
                    case "==":
                        return new BooleanValue(value == rightBoolean);
                    case "!=":
                        return new BooleanValue(value != rightBoolean);
                    default:
                        System.err.println("The operation " + operator + " isn't supported between two Booleans.");
                        System.exit(0);
                        return new NullValue();
                }
            case ValueType.String:
                String rightString = ((StringValue) right).getValue();
                switch (operator) {
                    case "+":
                        return new StringValue(value + rightString);
                    default:
                        System.err.println("The operation " + operator + " isn't supported between a Boolean and a String.");
                        System.exit(0);
                        return new NullValue();
                }

            default:
                System.err.println("Operations between a Boolean and a " + right.getType() + " are not supported.");
                System.exit(0);
                return new NullValue();
        }
    }
}
