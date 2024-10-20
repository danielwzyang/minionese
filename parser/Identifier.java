package parser;
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
        return "{ kind: " + super.kind + ", symbol: " + symbol + " }";
    }
}