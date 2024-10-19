package parser;
import java.util.Vector;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

public class Parser {
    private Vector<Token> tokens;

    public Program makeAST(String src) {
        Program program = new Program();

        Lexer lexer = new Lexer();
        tokens = lexer.tokenize(src);

        while (tokens.get(0).getType() != TokenType.EOF) {
            program.bodyPush(parseStatement());
        }

        return program;
    }

    private Stmt parseStatement() {
        return parseExpr();
    }
    
    private Expr parseExpr() {
        return parsePrimaryExpr();
    }

    private Expr parsePrimaryExpr() {
        Token token = popLeft();

        switch (token.getType()) {
            case TokenType.Identifier:
                return new Identifier(token.getValue());
            case TokenType.Number:
                return new NumericLiteral(Double.parseDouble(token.getValue()));
            
            default:
                System.err.println("Unexpected token found during parsing: " + token);
                return new Expr();
        }
    }

    private Token popLeft() {
        return tokens.removeFirst();
    }
}