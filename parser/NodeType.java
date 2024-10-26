package parser;
public enum NodeType {
    Program, UserMethod, 
    If, While, For,
    NumericLiteral,
    StringLiteral, SpliceExpr,
    Identifier,
    UnaryExpr, BinaryExpr,
    Declaration, Assignment,
    ObjectLiteral, Property,
    MemberExpr, CallExpr
}