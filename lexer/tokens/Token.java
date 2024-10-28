package lexer.tokens;

public class Token {
    private String value;
    private TokenType type;

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public Token(char value, TokenType type) {
        this.value = "" + value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public String toString() {
        return "{ " + value + ", " + type + " }";
    }
}