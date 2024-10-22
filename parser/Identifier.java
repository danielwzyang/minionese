package parser;

import runtime.Environment;
import runtime.RuntimeValue;

public class Identifier extends Expr {
    private String symbol;

    public Identifier(String symbol) {
        super(NodeType.Identifier);
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public String toString() {
        return "{ type: " + super.type + ", symbol: " + symbol + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        return environment.getVariableValue(symbol);
    }
}