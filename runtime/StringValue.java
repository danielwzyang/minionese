package runtime;

public class StringValue extends RuntimeValue {
    private String value;

    public StringValue(String value) {
        super(ValueType.String);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String valueToString() {
        return "" + value;
    }

    public String toString() {
        return "{ type: " + super.getType() + ", value: \"" + value + "\" }";
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        switch (right.getType()) {
            case ValueType.String:
                String rightString = ((StringValue) right).getValue();
                switch (operator) {
                    case "+":
                        return new StringValue(value + rightString);
                    case "==":
                        return new BooleanValue(value.equals(rightString));
                    default:
                        System.err.println("The operation " + operator + " isn't supported between a two Strings.");
                        System.exit(0);
                        return new NullValue();
                }
            case ValueType.Number:
                double rightDouble = ((NumberValue) right).getValue();
                switch (operator) {
                    case "+":
                        return new StringValue(value + rightDouble);
                    case "*":
                        if (rightDouble <= 0 || (int) rightDouble != rightDouble) {
                            System.err.println("Can only multiply strings by positive non-zero integers. Invalid number: " + rightDouble);
                            System.exit(0);
                        }
        
                        StringBuilder multipliedString = new StringBuilder();
        
                        for (int i = 0; i < rightDouble; i++)
                            multipliedString.append(value);
        
                        return new StringValue(multipliedString.toString());
                    default:
                        System.err.println("The operation " + operator + " isn't supported between a String and a Number.");
                        System.exit(0);
                        return new StringValue(null);
                }
            case ValueType.Boolean:
                boolean rightBoolean = ((BooleanValue) right).getValue();
                switch (operator) {
                    case "+":
                        return new StringValue(value + rightBoolean);
                    default:
                        System.err.println("The operation " + operator + " isn't supported between a String and a Boolean.");
                        System.exit(0);
                        return new NullValue();
                }
            default:
                System.err.println("Operations between a String and a " + right.getType() + " are not supported.");
                System.exit(0);
                return new NullValue();
        }
    }
}
