package interpreter;

import parser.*;

public class Interpreter {
    // we need to method overload evaluate since it can accept a bunch of types

    public RuntimeValue evaluate(Stmt astNode, Environment environment) {
        // this is to evaluate any given statement so it just checks the kind and calls the evaluate functions
        switch (astNode.getKind()) {
            case NodeType.NumericLiteral:
                return evaluate((NumericLiteral) astNode);
            case NodeType.BinaryExpr:
                return evaluate((BinaryExpr) astNode, environment);
            case NodeType.Identifier:
                return evaluate((Identifier) astNode, environment);
            case NodeType.Declaration:
                return evaluate((Declaration) astNode, environment);
            case NodeType.Assignment:
                return evaluate((Assignment) astNode, environment);
            default:
                System.err.println("This AST Node hasn't been setup for interpretation. Node: " + astNode);
                System.exit(0);
                return new NullVal();
        }
    }

    public RuntimeValue evaluate(Program program, Environment globalEnvironment) {
        // this is what's called when we run the code since we have to run the program through it
        // here we keep track of the last evaluated statement
        RuntimeValue last = new NullVal();

        for (Stmt statement : program.getBody()) {
            // goes through every statement and evaluates it
            last = evaluate(statement, globalEnvironment);
        }

        return last;
    }

    public RuntimeValue evaluate(NumericLiteral astNode) {
        // if the node is a number then we can just get the value
        return new NumberVal(astNode.getValue());
    }

    public RuntimeValue evaluate(BinaryExpr binOp, Environment environment) {
        // if the node is a binary expression then we can call evaluate for both the left and right side
        // since we have method overloads it doesn't matter what type the sides
        RuntimeValue left = evaluate(binOp.getLeft(), environment);
        RuntimeValue right = evaluate(binOp.getRight(), environment);

        // if both sides are numbers then we've boiled down to the simplest form
        if (left.getType() == ValueType.Number && right.getType() == ValueType.Number) {
            return evaluate((NumberVal) left, (NumberVal) right, binOp.getOperator());
        }

        return new NullVal();
    }

    public RuntimeValue evaluate(NumberVal left, NumberVal right, String operator) {
        // since the binexpr is at its simplest form we just need to do the operations
        switch (operator) {
            case "+":
                return new NumberVal(left.getValue() + right.getValue());
            case "-":
                return new NumberVal(left.getValue() - right.getValue());
            case "*":
                return new NumberVal(left.getValue() * right.getValue());
            case "/":
                if (right.getValue() == 0) {
                    System.err.println("Error: dividing by zero.");
                    System.exit(0);
                }
                return new NumberVal(left.getValue() / right.getValue());
            case "%":
                return new NumberVal(left.getValue() % right.getValue());
            case "^":
                return new NumberVal(Math.pow(left.getValue(), right.getValue()));
            default:
                System.err.println("Unexpected operator: " + operator);
                System.exit(0);
                return new NumberVal(0);
        }
    }

    // evaluates an identifier by checking the environment
    public RuntimeValue evaluate(Identifier identifier, Environment environment) {
        return environment.getVariableValue(identifier.getSymbol());
    }

    public RuntimeValue evaluate(Declaration declaration, Environment environment) {
        RuntimeValue value = new NullVal();

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
                case NodeType.Identifier:
                    value = evaluate((Identifier) declaration.getValue(), environment);
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
                default:
                    System.err.println("This value cannot be assigned to the identifier: " + assignment.getValue());
                    System.exit(0);
                    break;
            }
        } else {
            System.err.println("This expression cannot be assigned: " + assignment.getAssignedExpr());
            System.exit(0);
        }

        return new NullVal();
    }
}