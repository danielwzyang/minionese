package parser;
public class Identifier extends Expr {
    String symbol;

    public Identifier(String symbol) {
        super(NodeType.Identifier);
        this.symbol = symbol;
    }

    public String toString() {
        return "{ kind: " + super.kind + ", symbol: " + symbol + " }";
    }
}