package interpreter;
import parser.*;

public class Interpreter {
    public RuntimeValue evaluate(NumericLiteral astNode) {
        return new NumberVal(astNode.getValue());
    }

    public RuntimeValue evaluate(NullLiteral astNode) {
        return new NullVal();
    }

    public RuntimeValue evaluate(BinaryExpr binOp) {
        RuntimeValue left = evaluate(binOp.getLeft());
        RuntimeValue right = evaluate(binOp.getRight());

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

            default:
                System.err.println("Unexpected operator: " + operator);
                System.exit(0);
                return new NumberVal(0);
        }
    }

    public RuntimeValue evaluate(Program program) {
        RuntimeValue last = new NullVal();

        for (Stmt statement : program.getBody()) {
            last = evaluate(statement);
        }

        return last;
    }

    public RuntimeValue evaluate(Stmt astNode) {
        switch (astNode.getKind()) {
            case NodeType.BinaryExpr:
                return evaluate((BinaryExpr) astNode);
            case NodeType.NullLiteral:
                return new NullVal();
            case NodeType.NumericLiteral:
                return evaluate((NumericLiteral) astNode);
            default:
                System.err.println("This AST Node hasn't been setup for interpretation. Node: " + astNode);
                System.exit(0);
                return new NullVal();
        }
    }
}