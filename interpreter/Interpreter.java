package interpreter;

import parser.*;

public class Interpreter {
    public RuntimeValue evaluate(NumericLiteral astNode) {
        return new NumberVal(astNode.getValue());
    }

    public RuntimeValue evaluate(BinaryExpr binOp, Environment environment) {
        RuntimeValue left = evaluate(binOp.getLeft(), environment);
        RuntimeValue right = evaluate(binOp.getRight(), environment);

        if (left.getType() == ValueType.Number && right.getType() == ValueType.Number) {
            return evaluate((NumberVal) left, (NumberVal) right, binOp.getOperator());
        }

        return new NullVal();
    }

    public RuntimeValue evaluate(NumberVal left, NumberVal right, String operator) {
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

    public RuntimeValue evaluate(Program program, Environment globalEnvironment) {
        RuntimeValue last = new NullVal();

        for (Stmt statement : program.getBody()) {
            last = evaluate(statement, globalEnvironment);
        }

        return last;
    }

    public RuntimeValue evaluate(Identifier identifier, Environment environment) {
        return environment.getVariableValue(identifier.getSymbol());
    }

    public RuntimeValue evaluate(Declaration declaration, Environment environment) {
        RuntimeValue value = new NullVal();

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
                    System.err.println("This declaration expression hasn't been setup for interpretation. Expression: "
                            + declaration);
                    System.exit(0);
                    break;
            }
        }

        environment.declareVariable(declaration.getIdentifier(), value, declaration.getIsFinal());
        return environment.getVariableValue(declaration.getIdentifier());
    }

    public RuntimeValue evaluate(Assignment assignment, Environment environment) {
        if (assignment.getAssignedExpr().getKind() == NodeType.Identifier) {
            String symbol = ((Identifier) assignment.getAssignedExpr()).getSymbol();

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

    public RuntimeValue evaluate(Stmt astNode, Environment environment) {
        switch (astNode.getKind()) {
            case NodeType.BinaryExpr:
                return evaluate((BinaryExpr) astNode, environment);
            case NodeType.NumericLiteral:
                return evaluate((NumericLiteral) astNode);
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
}