package interpreter;

import parser.*;

public class Interpreter {
    // we need to method overload evaluate since it can accept a bunch of types

    public RuntimeValue evaluate(Stmt astNode, Environment environment) {
        // this is to evaluate any given statement so it just checks the kind and calls the evaluate functions
        switch (astNode.getKind()) {
            case NodeType.NumericLiteral:
                return evaluate((NumericLiteral) astNode);
            case NodeType.StringLiteral:
                return evaluate((StringLiteral) astNode);
            case NodeType.BinaryExpr:
                return evaluate((BinaryExpr) astNode, environment);
            case NodeType.Identifier:
                return evaluate((Identifier) astNode, environment);
            case NodeType.ObjectLiteral:
                return evaluate((ObjectLiteral) astNode, environment);
            case NodeType.Declaration:
                return evaluate((Declaration) astNode, environment);
            case NodeType.Assignment:
                return evaluate((Assignment) astNode, environment);
            default:
                System.err.println("This AST Node hasn't been setup for interpretation. Node: " + astNode);
                System.exit(0);
                return new NullValue();
        }
    }

    public RuntimeValue evaluate(Program program, Environment globalEnvironment) {
        // this is what's called when we run the code since we have to run the program through it
        // here we keep track of the last evaluated statement
        RuntimeValue last = new NullValue();

        for (Stmt statement : program.getBody()) {
            // goes through every statement and evaluates it
            last = evaluate(statement, globalEnvironment);
        }

        return last;
    }

    public RuntimeValue evaluate(NumericLiteral astNode) {
        // if the node is a number then we can just get the value
        return new NumberValue(astNode.getValue());
    }

    public RuntimeValue evaluate(StringLiteral astNode) {
        // if the node is a string then we can just get the value
        return new StringValue(astNode.getValue());
    }

    public RuntimeValue evaluate(BinaryExpr binOp, Environment environment) {
        // if the node is a binary expression then we can call evaluate for both the left and right side
        // since we have method overloads it doesn't matter what type the sides
        RuntimeValue left = evaluate(binOp.getLeft(), environment);
        RuntimeValue right = evaluate(binOp.getRight(), environment);

        String operator = binOp.getOperator();

        // if both sides are numbers then we've boiled down to the simplest form
        if (left.getType() == ValueType.Number && right.getType() == ValueType.Number) {
            return evaluate((NumberValue) left, (NumberValue) right, operator);
        }

        // if the left side is a string then we have to handle a lot of cases like types converting and string multiplication etc.
        if (left.getType() == ValueType.String) {
            switch (right.getType()) {
                case ValueType.String:
                    return evaluate((StringValue) left, (StringValue) right, operator);
                case ValueType.Number:
                    return evaluate((StringValue) left, (NumberValue) right, operator);
                default:
                    System.err.println("Binary operations between a String and " + right.getType() + " are not supported.");
            }
        }
        
        System.err.println("Operation between two types in order " + left.getType() + " and " + right.getType() + " is not supported. Returning null value instead.");
        return new NullValue();
    }

    public RuntimeValue evaluate(NumberValue left, NumberValue right, String operator) {
        // since the binexpr is at its simplest form we just need to do the operations
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

    public RuntimeValue evaluate(StringValue left, NumberValue right, String operator) {
        switch (operator) {
            case "+":
                return new StringValue(left.getValue() + right.getValue());
            case "*":
                if (right.getValue() <= 0 || (int) right.getValue() != right.getValue()) {
                    System.err.println("Can only multiply strings by positive non-zero integers. Invalid number: " + right.getValue());
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

    public RuntimeValue evaluate(Identifier identifier, Environment environment) {
        // evaluates an identifier by checking the environment
        return environment.getVariableValue(identifier.getSymbol());
    }

    public RuntimeValue evaluate(Declaration declaration, Environment environment) {
        RuntimeValue value = new NullValue();

        // we need to check if the value is null or not because it can cause an error with getKind()
        // value is initialized as a null value to handle the case that the value is assigned to null
        if (declaration.getValue() != null) {
            switch (declaration.getValue().getKind()) {
                case NodeType.BinaryExpr:
                    value = evaluate((BinaryExpr) declaration.getValue(), environment);
                    break;
                case NodeType.NumericLiteral:
                    value = evaluate((NumericLiteral) declaration.getValue());
                    break;
                case NodeType.StringLiteral:
                    value = evaluate((StringLiteral) declaration.getValue());
                    break;
                case NodeType.Identifier:
                    value = evaluate((Identifier) declaration.getValue(), environment);
                    break;
                case NodeType.ObjectLiteral:
                    value = evaluate((ObjectLiteral) declaration.getValue(), environment);
                    break;
                default:
                    System.err.println("This declaration expression hasn't been setup for interpretation. Expression: " + declaration);
                    System.exit(0);
                    break;
            }
        }

        // we declare the variable in the environment
        return environment.declareVariable(declaration.getIdentifier(), value, declaration.getIsFinal());
    }

    public RuntimeValue evaluate(Assignment assignment, Environment environment) {
        // here we're just handling if the assigned expression is an identifier
        if (assignment.getAssignedExpr().getKind() == NodeType.Identifier) {
            String symbol = ((Identifier) assignment.getAssignedExpr()).getSymbol();

            // based on what kind the value is we need to call different evaluate functions
            switch (assignment.getValue().getKind()) {
                case NodeType.BinaryExpr:
                    return environment.assignVariable(symbol, evaluate((BinaryExpr) assignment.getValue(), environment));
                case NodeType.NumericLiteral:
                    return environment.assignVariable(symbol, evaluate((NumericLiteral) assignment.getValue(), environment));
                case NodeType.Identifier:
                    return environment.assignVariable(symbol, evaluate((Identifier) assignment.getValue(), environment));
                case NodeType.Assignment:
                    return environment.assignVariable(symbol, evaluate(assignment.getValue(), environment));
                case NodeType.ObjectLiteral:
                    return environment.assignVariable(symbol, evaluate((ObjectLiteral) assignment.getValue(), environment));
                default:
                    System.err.println("This value cannot be assigned to the identifier: " + assignment.getValue());
                    System.exit(0);
                    break;
            }
        } else {
            System.err.println("This expression cannot be assigned: " + assignment.getAssignedExpr());
            System.exit(0);
        }

        return new NullValue();
    }

    public RuntimeValue evaluate(ObjectLiteral objectLiteral, Environment environment) {
        ObjectValue object = new ObjectValue();
        for (Property property : objectLiteral.getProperties()) {
            // if value is null then we're using { key } otherwise it's { key : value }
            // if it's { key } then it's a variable so we get it from the environment
            // otherwise we evaluate the value expression
            RuntimeValue value = property.getValue() == null ? environment.getVariableValue(property.getKey()) : evaluate(property.getValue(), environment);

            object.addProperty(property.getKey(), value);
        }

        return object;
    }
}