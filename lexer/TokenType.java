package lexer;
public enum TokenType {
    Null,
    EOF,
    Number,
    String,
    Define, Final,
    Equivalence, BinOp,
    Increment, Negation,
    If, While, For,
    Identifier,
    Assignment,
    OpenP, CloseP,
    OpenBrace, CloseBrace,
    OpenBracket, CloseBracket,
    Comma, Colon, Dot,
}