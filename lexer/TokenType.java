package lexer;
public enum TokenType {
    Null,
    EOF,
    Number,
    String,
    Define, Final,
    Identifier,
    Equals,
    OpenP, CloseP,
    OpenBrace, CloseBrace,
    OpenBracket, CloseBracket,
    Comma, Colon, Dot,
    BinOp,
}