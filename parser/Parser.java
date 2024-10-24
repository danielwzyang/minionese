package parser;

import java.util.List;
import java.util.ArrayList;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

public class Parser {
    private List<Token> tokens;

    private Token popLeft() {
        return tokens.removeFirst();
    }

    private Token popLeft(TokenType expected, String error) {
        // this does the same thing as the other popLeft but it will throw an error if
        // the token isn't the type it expects
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
            // if the token is final then this doesn't work since we can't define a final
            // with no value
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
     * these go in order of precedence ( first to last )
     * primary
     * call
     * member
     * splicing / retrieval
     * exponential (^)
     * multiplicative (*, /, %)
     * additive (+, -)
     * comparitive (&, |, ==)
     * object
     * assignment (=)
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
            case TokenType.String:
                return new StringLiteral(token.getValue());
            default:
                System.err.println("Unexpected token found during parsing: " + token);
                System.exit(0);
                return new Identifier(null);
        }
    }

    private Expr parseCallOrMemberExpr() {
        Expr member = parseMemberExpr();

        // foo()
        if (tokens.get(0).getType() == TokenType.OpenP) {
            return parseCallExpr(member);
        }

        return member;
    }

    private Expr parseMemberExpr() {
        Expr object = parsePrimaryExpr();

        // handles either foo.bar or foo["bar"]; while loop allows for chaining
        while (tokens.get(0).getType() == TokenType.Dot || tokens.get(0).getType() == TokenType.OpenBracket ) {
            Token operator = popLeft(); // gets either the dot or the bracket
            Expr property;
            boolean computed;

            // foo.bar
            if (operator.getType() == TokenType.Dot) {
                computed = false;
                property = parsePrimaryExpr(); // identifier gets parsed as a primary expr

                if (property.getType() != NodeType.Identifier) {
                    System.err.println("Expected identifier after dot operator but instead received: " + property.getType());
                }
            }
            // foo[bar] or str[x] / str[x:y] / str[x:] / str[:y]
            else {
                computed = true;

                // str[:y]
                if (tokens.get(0).getType() == TokenType.Colon) {
                    popLeft(); // pops colon
                    object = new SpliceExpr(object, new NumericLiteral(0), parseExpr());
                    popLeft(TokenType.CloseBracket, "Expected closing bracket after character retrieval from string.");
                    continue;
                }

                property = parseExpr(); // either property of object or left bound for splice

                // str[x:] or str[x:y]
                if (tokens.get(0).getType() == TokenType.Colon) {
                    popLeft(); // pops colon
                    
                    // str[x:]
                    if (tokens.get(0).getType() == TokenType.CloseBracket) {
                        popLeft(); // gets rid of closed bracket
                        object = new SpliceExpr(object, property);
                    }
                    // str[x:y]
                    else {
                        object = new SpliceExpr(object, property, parseExpr());
                        popLeft(TokenType.CloseBracket, "Expected closing bracket after splice expr.");
                    }
                    
                    continue;
                }

                popLeft(TokenType.CloseBracket, "Expected closing bracket after computed member call.");
            }

            object = new MemberExpr(object, property, computed);
        }

        return object;
    }

    private Expr parseCallExpr(Expr caller) {
        CallExpr callExpr = new CallExpr(parseArguments(), caller);

        // handles calls like func()() where the first function returns another function
        if (tokens.get(0).getType() == TokenType.OpenP) {
            callExpr = (CallExpr) parseCallExpr(caller);
        }

        return callExpr;
    }

    private Expr[] parseArguments() {
        popLeft(); // gets rid of open parentheses

        if (tokens.get(0).getType() == TokenType.CloseP) {
            popLeft(); // gets rid of closed parentheses
            return new Expr[0];
        }

        List<Expr> arguments = new ArrayList<Expr>();
        arguments.add(parseExpr()); // parses first argument so we're on the comma now

        while (tokens.get(0).getType() == TokenType.Comma) {
            popLeft(); // gets rid of comma
            arguments.add(parseExpr());
        }
        
        popLeft(TokenType.CloseP, "Expected closing parentheses after function call.");
        return arguments.toArray(new Expr[0]);
    }

    private Expr parseExponentialExpr() {
        // we always call the function that's above in the order of precedence
        Expr left = parseCallOrMemberExpr();

        // basically keeps evaluating the expression and adding it to the right
        while (tokens.get(0).getValue().equals("^")) {
            // now we hit the operator so we want to pop that token
            String operator = popLeft().getValue();
            // the next token is the next expression
            Expr right = parseCallOrMemberExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseMultiplicativeExpr() {
        // the exact same as the exponential function just with multiplicative operators
        Expr left = parseExponentialExpr();

        while (tokens.get(0).getValue().equals("/") || tokens.get(0).getValue().equals("*")
                || tokens.get(0).getValue().equals("%")) {
            String operator = popLeft().getValue();
            Expr right = parseExponentialExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseAdditiveExpr() {
        // the exact same as the multiplicative fuction just with additive operators
        Expr left = parseMultiplicativeExpr();

        while (tokens.get(0).getValue().equals("+") || tokens.get(0).getValue().equals("-")) {
            String operator = popLeft().getValue();
            Expr right = parseMultiplicativeExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseComparitiveExpr() {
        // the exact same as additive function just with comparitive operators
        Expr left = parseAdditiveExpr();

        while (tokens.get(0).getValue().equals("&") || tokens.get(0).getValue().equals("|")
                || tokens.get(0).getValue().equals("==")) {
            String operator = popLeft().getValue();
            Expr right = parseAdditiveExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseObject() {
        // if we're not at an open brace then we continue up the order of precedence
        if (tokens.get(0).getType() != TokenType.OpenBrace)
            return parseComparitiveExpr();

        popLeft(); // pops open brace
        List<Property> properties = new ArrayList<Property>();

        // we want to keep fetching the properties until we reach the close bracket or
        // the end of file
        while (tokens.get(0).getType() != TokenType.EOF && tokens.get(0).getType() != TokenType.CloseBrace) {
            // first case second case
            // objects can either be { key: val, key2: val } or { key, key2 } (passing in
            // variables)

            // gets key
            Token key = popLeft(TokenType.Identifier, "Object key expected.");

            // if there's a comma (second case of object definition described above)
            if (tokens.get(0).getType() == TokenType.Comma) {
                popLeft(); // pops comma
                properties.add(new Property(key.getValue()));
                continue;
            }

            // if there's no comma (second case)
            if (tokens.get(0).getType() == TokenType.CloseBrace) {
                properties.add(new Property(key.getValue()));
                continue;
            }

            // first case
            popLeft(TokenType.Colon, "Colon expected after key."); // pops colon
            properties.add(new Property(key.getValue(), parseExpr()));

            if (tokens.get(0).getType() != TokenType.CloseBrace)
                popLeft(TokenType.Comma, "Comma or close brace expected after property."); // pops comma
        }

        popLeft(TokenType.CloseBrace, "Closing brace expected.");
        return new ObjectLiteral(properties);
    }

    private Expr parseAssignment() {
        // gets the expression that we're assigning
        Expr left = parseObject();

        while (tokens.get(0).getType() == TokenType.Equals) {
            popLeft(); // pop the equals sign
            Expr value = parseAssignment(); // evaluate the rigth hand side

            // returns assignment to allow chains like x = y = z
            left = new Assignment(left, value);
        }

        return left;
    }
}