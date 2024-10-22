package parser;
public enum NodeType {
    Program,
    NumericLiteral,
    StringLiteral,
    Identifier,
    BinaryExpr,
    Declaration, Assignment,
    ObjectLiteral, Property,
    MemberExpr, CallExpr
}