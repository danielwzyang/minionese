package parser.literal;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.values.RuntimeValue;

public class Identifier extends Expr {
    private String symbol;

    public Identifier(String symbol) {
        super(NodeType.Identifier);
        this.symbol = symbol;
    }

    public String getName() {
        return symbol;
    }

    public String toString() {
        return "{ type: " + super.type + ", symbol: " + symbol + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        return environment.getVariableValue(symbol);
    }
}