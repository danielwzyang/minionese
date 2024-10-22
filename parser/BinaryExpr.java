package parser;

import runtime.BooleanValue;
import runtime.Environment;
import runtime.NullValue;
import runtime.NumberValue;
import runtime.RuntimeValue;
import runtime.StringValue;
import runtime.ValueType;

public class BinaryExpr extends Expr {
    private Expr left, right;
    private String operator;

    public BinaryExpr(Expr left, Expr right, String operator) {
        super(NodeType.BinaryExpr);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", left: " + left + ", right: " + right + ", operator: " + operator + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        // if the node is a binary expression then we can call evaluate for both the
        // left and right side
        // since we have method overloads it doesn't matter what type the sides
        RuntimeValue left = this.left.evaluate(environment);
        RuntimeValue right = this.right.evaluate(environment);

        // if the left side is a number then we have to handle the right side being
        // different types
        if (left.getType() == ValueType.Number) {
            switch (right.getType()) {
                case ValueType.Number:
                    return evaluate((NumberValue) left, (NumberValue) right, operator);
                case ValueType.String:
                    return evaluate((NumberValue) left, (StringValue) right, operator);
                default:
                    System.err.println(
                            "Binary operations between a String and " + right.getType() + " are not supported.");
            }

        }

        // left side is a string, handle the right side being different types
        if (left.getType() == ValueType.String) {
            switch (right.getType()) {
                case ValueType.String:
                    return evaluate((StringValue) left, (StringValue) right, operator);
                case ValueType.Number:
                    return evaluate((StringValue) left, (NumberValue) right, operator);
                case ValueType.Boolean:
                    return evaluate((StringValue) left, (BooleanValue) right, operator);
                default:
                    System.err.println(
                            "Binary operations between a String and " + right.getType() + " are not supported.");
            }
        }

        System.err.println("The operation " + operator + " between " + left.getType() + " and " + right.getType()
                + " is not supported. Returning null value instead.");
        return new NullValue();
    }

    public RuntimeValue evaluate(NumberValue left, NumberValue right, String operator) {
        switch (operator) {
            case "+":
                return new NumberValue(left.getValue() + right.getValue());
            case "-":
                return new NumberValue(left.getValue() - right.getValue());
            case "*":
                return new NumberValue(left.getValue() * right.getValue());
            case "/":
                if (right.getValue() == 0) {
                    System.err.println("Error: dividing by zero.");
                    System.exit(0);
                }
                return new NumberValue(left.getValue() / right.getValue());
            case "%":
                return new NumberValue(left.getValue() % right.getValue());
            case "^":
                return new NumberValue(Math.pow(left.getValue(), right.getValue()));
            default:
                System.err.println("The operation " + operator + " isn't supported between two Numbers.");
                System.exit(0);
                return new NumberValue(0);
        }
    }

    public RuntimeValue evaluate(StringValue left, StringValue right, String operator) {
        switch (operator) {
            case "+":
                return new StringValue(left.getValue() + right.getValue());
            default:
                System.err.println("The operation " + operator + " isn't supported between a two Strings.");
                System.exit(0);
                return new StringValue(null);
        }
    }

    public RuntimeValue evaluate(NumberValue left, StringValue right, String operator) {
        switch (operator) {
            case "+":
                return new StringValue(left.getValue() + right.getValue());
            case "*":
                if (left.getValue() <= 0 || (int) left.getValue() != left.getValue()) {
                    System.err.println("Can only multiply strings by positive non-zero integers. Invalid number: "
                            + left.getValue());
                    System.exit(0);
                }

                StringBuilder multipliedString = new StringBuilder();

                for (int i = 0; i < left.getValue(); i++)
                    multipliedString.append(right.getValue());

                return new StringValue(multipliedString.toString());
            default:
                System.err.println("The operation " + operator + " isn't supported between a Number and a String.");
                System.exit(0);
                return new StringValue(null);

        }
    }

    public RuntimeValue evaluate(StringValue left, NumberValue right, String operator) {
        switch (operator) {
            case "+":
                return new StringValue(left.getValue() + right.getValue());
            case "*":
                if (right.getValue() <= 0 || (int) right.getValue() != right.getValue()) {
                    System.err.println("Can only multiply strings by positive non-zero integers. Invalid number: "
                            + right.getValue());
                    System.exit(0);
                }

                StringBuilder multipliedString = new StringBuilder();

                for (int i = 0; i < right.getValue(); i++)
                    multipliedString.append(left.getValue());

                return new StringValue(multipliedString.toString());
            default:
                System.err.println("The operation " + operator + " isn't supported between a String and a Number.");
                System.exit(0);
                return new StringValue(null);

        }
    }

    public RuntimeValue evaluate(StringValue left, BooleanValue right, String operator) {
        switch (operator) {
            case "+":
                return new StringValue(left.getValue() + right.getValue());
            default:
                System.err.println("The operation " + operator + " isn't supported between a String and a Boolean.");
                System.exit(0);
                return new StringValue(null);

        }
    }
}
