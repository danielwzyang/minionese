package parser.expr;

import parser.NodeType;
import parser.literal.NumericLiteral;
import runtime.Environment;
import runtime.ValueType;
import runtime.values.BooleanValue;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;

public class UnaryExpr extends Expr {
    String operator;
    Expr expr;

    public UnaryExpr(String operator, Expr expr) {
        super(NodeType.UnaryExpr);
        this.operator = operator;
        this.expr = expr;
    }

    public RuntimeValue evaluate(Environment environment) {
        switch (operator) {
            case "!":
                RuntimeValue exprValue = expr.evaluate(environment);
                if (exprValue.getType() != ValueType.Boolean) {
                    System.err.println("Expected boolean after ! operator.");
                    System.exit(0);
                }
                return new BooleanValue(!((BooleanValue) exprValue).getValue());
            case "++":
                Assignment incrementedValue = new Assignment(expr, new BinaryExpr(expr, new NumericLiteral(1), "+"));
                return incrementedValue.evaluate(environment);
            case "--":
                Assignment decrementedValue = new Assignment(expr, new BinaryExpr(expr, new NumericLiteral(1), "-"));
                return decrementedValue.evaluate(environment);
            default:
                System.err.println("This unary operator isn't supported.");
                System.exit(0);
                return new NullValue();
        }
    }
}
