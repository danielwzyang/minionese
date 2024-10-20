package parser;
import java.util.List;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

public class Parser {
    private List<Token> tokens;
    
    private Token popLeft() {
        return tokens.removeFirst();
    }

    private Token popLeft(TokenType expected, String error) {
        Token prev = tokens.removeFirst();
        if (prev.getType() != expected) {
            System.err.println(error);
            System.exit(0);
        }

        return prev;
    }

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
        return parseAdditiveExpr();
    }

    /*
        these go in order of precedence ( first to last )
            primary
            multiplicative (*, /, %)
            additive (+, -)
    */

    private Expr parsePrimaryExpr() {
        Token token = popLeft();

        switch (token.getType()) {
            case TokenType.Identifier:
                return new Identifier(token.getValue());
            case TokenType.Number:
                return new NumericLiteral(Double.parseDouble(token.getValue()));
            case TokenType.OpenP:
                Expr value = parseExpr(); // evaluate expression inside parentheses
                popLeft(TokenType.CloseP, "Unexpected token type instead of closed parenthesis."); // pop closed parentheses
                return value;
            case TokenType.Null:
                return new NullLiteral();

            default:
                System.err.println("Unexpected token found during parsing: " + token);
                return new Expr();
        }
    }

    private Expr parseAdditiveExpr() {
        Expr left = parseMultiplicativeExpr();

        while (tokens.get(0).getValue().equals("+") || tokens.get(0).getValue().equals("-")) {
            String operator = popLeft().getValue();
            Expr right = parseMultiplicativeExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseMultiplicativeExpr() {
        // we always call the function that's above in the order of precedence
        Expr left = parsePrimaryExpr();

        // basically keeps evaluating the expression and adding it to the right
        while (tokens.get(0).getValue().equals("/") || tokens.get(0).getValue().equals("*") || tokens.get(0).getValue().equals("%")) {
            // now we hit the operator so we want to pop that token
            String operator = popLeft().getValue();
            // the next token is the next expression
            Expr right = parsePrimaryExpr();

            // sets the left to the binary expression since left comes first
            left = new BinaryExpr(left, right, operator);   
        }

        // returns left now that it's a binary expression
        return left;
    }
}