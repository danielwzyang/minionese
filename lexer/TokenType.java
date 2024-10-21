package lexer;
public enum TokenType {
    Null,
    EOF,
    Number,
    Quote, String,
    Define, Final,
    Identifier,
    Equals,
    OpenP, CloseP,
    OpenBrace, CloseBrace,
    Comma, Colon,
    BinOp,
}