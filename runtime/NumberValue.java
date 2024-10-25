package runtime;

public class NumberValue extends RuntimeValue {
    private double value;

    public NumberValue() {
        super(ValueType.Number);
        value = 0;
    }

    public NumberValue(double value) {
        super(ValueType.Number);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "" + value;
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        switch (right.getType()) {
            case ValueType.Number:
                double rightDouble = ((NumberValue) right).getValue();
                switch (operator) {
                    case "+":
                        return new NumberValue(value + rightDouble);
                    case "-":
                        return new NumberValue(value - rightDouble);
                    case "*":
                        return new NumberValue(value * rightDouble);
                    case "/":
                        if (rightDouble == 0) {
                            System.err.println("Error: dividing by zero.");
                            System.exit(0);
                        }
                        return new NumberValue(value / rightDouble);
                    case "//":
                        if (rightDouble == 0) {
                            System.err.println("Error: dividing by zero.");
                            System.exit(0);
                        }
                        return new NumberValue((int) (value / rightDouble));
                    case "%":
                        return new NumberValue(value % rightDouble);
                    case "^":
                        return new NumberValue(Math.pow(value, rightDouble));
                    case "==":
                        return new BooleanValue(value == rightDouble);
                    case "!=":
                        return new BooleanValue(value != rightDouble);
                    case "<":
                        return new BooleanValue(value < rightDouble);
                    case "<=":
                        return new BooleanValue(value <= rightDouble);
                    case ">":
                        return new BooleanValue(value > rightDouble);
                    case ">=":
                        return new BooleanValue(value >= rightDouble);
                    default:
                        System.err.println("The operation " + operator + " isn't supported between two Numbers.");
                        System.exit(0);
                        return new NullValue();
                }

            case ValueType.String:
                String rightString = ((StringValue) right).getValue();
                switch (operator) {
                    case "+":
                        return new StringValue(value + rightString);
                    case "*":
                        if (value <= 0 || (int) value != value) {
                            System.err
                                    .println("Can only multiply strings by positive non-zero integers. Invalid number: "
                                            + value);
                            System.exit(0);
                        }

                        StringBuilder multipliedString = new StringBuilder();

                        for (int i = 0; i < value; i++)
                            multipliedString.append(rightString);

                        return new StringValue(multipliedString.toString());
                    default:
                        System.err.println(
                                "The operation " + operator + " isn't supported between a Number and a String.");
                        System.exit(0);
                        return new NullValue();
                }
            default:
                System.err.println("Operations between a Number and a " + right.getType() + " are not supported.");
                System.exit(0);
                return new NullValue();
        }
    }
}
