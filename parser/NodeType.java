package parser;
public enum NodeType {
    Program,
    NumericLiteral,
    StringLiteral, SpliceExpr,
    Identifier,
    UnaryExpr, BinaryExpr,
    Declaration, Assignment,
    ObjectLiteral, Property,
    MemberExpr, CallExpr
}