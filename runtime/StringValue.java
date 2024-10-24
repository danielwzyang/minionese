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
        return value;
    }

    public String toString() {
        return "{ type: " + super.getType() + ", value: \"" + value + "\" }";
    }

    public RuntimeValue splice(RuntimeValue left, RuntimeValue right, Environment environment) {
        if (left.getType() != ValueType.Number || right.getType() != ValueType.Number) {
            System.err.println("Cannot splice with non-number indices.");
            System.exit(0);
        }

        int leftBound = (int) ((NumberValue) left).getValue();
        int rightBound = (int) ((NumberValue) right).getValue();

        if (leftBound != ((NumberValue) left).getValue() || rightBound != ((NumberValue) right).getValue()) {
            System.err.println("Cannot splice with non-integer indices.");
            System.exit(0);
        }

        if (leftBound < 0 || rightBound > value.length()) {
            System.err.println("Index out of bounds while splicing.");
            System.exit(0);
        }

        return new StringValue(value.substring(leftBound, rightBound));
    }

    public RuntimeValue splice(RuntimeValue index, Environment environment) {
        if (index.getType() != ValueType.Number) {
            System.err.println("Cannot retrieve character with non-number index.");
            System.exit(0);
        }

        int leftBound = (int) ((NumberValue) index).getValue();

        if (leftBound != ((NumberValue) index).getValue()) {
            System.err.println("Cannot retrieve character at non-integer index.");
            System.exit(0);
        }

        if (leftBound < 0 || leftBound >= value.length()) {
            System.err.println("Index out of bounds while retrieving character.");
            System.exit(0);
        }

        return new StringValue(value.substring(leftBound, leftBound + 1));
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
                            System.err
                                    .println("Can only multiply strings by positive non-zero integers. Invalid number: "
                                            + rightDouble);
                            System.exit(0);
                        }

                        StringBuilder multipliedString = new StringBuilder();

                        for (int i = 0; i < rightDouble; i++)
                            multipliedString.append(value);

                        return new StringValue(multipliedString.toString());
                    default:
                        System.err.println(
                                "The operation " + operator + " isn't supported between a String and a Number.");
                        System.exit(0);
                        return new StringValue(null);
                }
            case ValueType.Boolean:
                boolean rightBoolean = ((BooleanValue) right).getValue();
                switch (operator) {
                    case "+":
                        return new StringValue(value + rightBoolean);
                    default:
                        System.err.println(
                                "The operation " + operator + " isn't supported between a String and a Boolean.");
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
