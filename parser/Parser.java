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
        // this does the same thing as the other popLeft but it will throw an error if the token isn't the type it expects
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
        switch (tokens.get(0).getType()) {
            case TokenType.Define:
                return parseDeclaration();
            case TokenType.Final:
                return parseDeclaration();
            default:
                return parseExpr();
        }
    }

    private Declaration parseDeclaration() {
        Boolean isFinal = popLeft().getType() == TokenType.Final;
        String identifier = popLeft(TokenType.Identifier, "Identifier expected.").getValue();

        // if there's no value provided
        if (tokens.get(0).getType() != TokenType.Equals) {
            // if the token is final then this doesn't work since we can't define a final with no value
            if (isFinal) {
                System.err.println("Value expected for final definition of " + identifier + ".");
                System.exit(0);
            }
            
            return new Declaration(identifier, isFinal);
        }

        popLeft(); // pops equal sign

        // after the equals sign is the expression for the value
        Declaration declaration = new Declaration(identifier, isFinal, parseExpr());

        return declaration;
    }

    /*
        these go in order of precedence ( first to last )
            primary
            exponential (^)
            multiplicative (*, /, %)
            additive (+, -)
            assignment (=)
    */
    
    private Expr parseExpr() {
        // this always calls the one in the bottom of the order of precedence
        return parseAssignment();
    }

    private Expr parsePrimaryExpr() {
        Token token = popLeft();

        switch (token.getType()) {
            case TokenType.Identifier:
                return new Identifier(token.getValue());
            case TokenType.Number:
                return new NumericLiteral(Double.parseDouble(token.getValue()));
            case TokenType.OpenP:
                Expr value = parseExpr(); // evaluate expression inside parentheses
                popLeft(TokenType.CloseP, "Closed parentheses expected."); // pop closed parentheses
                return value;

            default:
                System.err.println("Unexpected token found during parsing: " + token);
                return new Expr();
        }
    }

    private Expr parseExponentialExpr() {
        // we always call the function that's above in the order of precedence
        Expr left = parsePrimaryExpr();

        // basically keeps evaluating the expression and adding it to the right
        while (tokens.get(0).getValue().equals("^")) {
            // now we hit the operator so we want to pop that token
            String operator = popLeft().getValue();
            // the next token is the next expression
            Expr right = parsePrimaryExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseMultiplicativeExpr() {
        // the exact same as the exponential function just with multiplicative operators
        Expr left = parseExponentialExpr();

        while (tokens.get(0).getValue().equals("/") || tokens.get(0).getValue().equals("*") || tokens.get(0).getValue().equals("%")) {
            String operator = popLeft().getValue();
            Expr right = parseExponentialExpr();

            left = new BinaryExpr(left, right, operator);   
        }

        return left;
    }

    private Expr parseAdditiveExpr() {
        // exact same as above but with additive
        Expr left = parseMultiplicativeExpr();

        while (tokens.get(0).getValue().equals("+") || tokens.get(0).getValue().equals("-")) {
            String operator = popLeft().getValue();
            Expr right = parseMultiplicativeExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseAssignment() {
        // gets the expression that we're assigning
        Expr left = parseAdditiveExpr();

        while (tokens.get(0).getType() == TokenType.Equals) {
            popLeft(); // pop the equals sign
            Expr value = parseAssignment(); // evaluate the rigth hand side
            
            // returns assignment to allow chains like x = y = z
            left = new Assignment(left, value);
        }

        return left;
    }
}