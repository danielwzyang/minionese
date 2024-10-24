package parser;
public enum NodeType {
    Program,
    NumericLiteral,
    StringLiteral, SpliceExpr,
    Identifier,
    BinaryExpr,
    Declaration, Assignment,
    ObjectLiteral, Property,
    MemberExpr, CallExpr
}