package parser;
public enum NodeType {
    Program, UserMethod, 
    If, While, For,
    Break, Continue,
    NumericLiteral,
    StringLiteral, SpliceExpr,
    Identifier,
    Array,
    UnaryExpr, BinaryExpr,
    MethodDeclaration, Declaration, Assignment,
    ObjectLiteral, Property,
    MemberExpr, CallExpr,
    Return
}