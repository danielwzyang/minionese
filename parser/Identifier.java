package parser;

public class Identifier extends Expr {
    String symbol;

    public Identifier(String symbol) {
        super(NodeType.Identifier);
        this.symbol = symbol;
    }
}