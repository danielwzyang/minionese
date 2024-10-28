package lexer.tokens;
public enum TokenType {
    Null,
    EOF,
    Number,
    String,
    Return,
    Define, Final, DefineMethod,
    Equivalence, BinOp,
    Increment, Negation,
    If, Else, While, For,
    Break, Continue,
    Identifier,
    Assignment,
    OpenP, CloseP,
    OpenBrace, CloseBrace,
    OpenBracket, CloseBracket,
    Comma, Colon, Dot,
}