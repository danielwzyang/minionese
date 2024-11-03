package parser;

import java.util.ArrayList;

import lexer.tokens.Token;
import lexer.tokens.TokenType;
import parser.expr.Assignment;
import parser.expr.BinaryExpr;
import parser.expr.CallExpr;
import parser.expr.Expr;
import parser.expr.MemberExpr;
import parser.expr.SpliceExpr;
import parser.expr.UnaryExpr;
import parser.literal.Array;
import parser.literal.Identifier;
import parser.literal.NumericLiteral;
import parser.literal.ObjectLiteral;
import parser.literal.Property;
import parser.literal.StringLiteral;

public class ExprParser {
    private ArrayList<Token> tokens;

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

    public ExprParser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    /*
     * these go in order of precedence ( first to last )
     * primary
     * call / member / splicing
     * splicing / retrieval
     * increment operations (++, --) / negation operation (!)
     * exponential (^)
     * multiplicative (*, /, %)
     * additive (+, -)
     * equivalence (==)
     * comparitive (&, |, >, <, >=, <=)
     * object / array
     * assignment (=)
     */

    public Expr parseExpr() {
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
        while (tokens.get(0).getType() == TokenType.Dot || tokens.get(0).getType() == TokenType.OpenBracket) {
            Token operator = popLeft(); // gets either the dot or the bracket
            Expr property;
            boolean computed;

            // foo.bar
            if (operator.getType() == TokenType.Dot) {
                computed = false;
                property = parsePrimaryExpr(); // identifier gets parsed as a primary expr

                if (property.getType() != NodeType.Identifier) {
                    System.err.println(
                            "Expected identifier after dot operator but instead received: " + property.getType());
                    System.exit(0);
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

    public Expr[] parseArguments() {
        popLeft(); // gets rid of open parentheses

        if (tokens.get(0).getType() == TokenType.CloseP) {
            popLeft(); // gets rid of closed parentheses
            return new Expr[0];
        }

        ArrayList<Expr> arguments = new ArrayList<Expr>();
        arguments.add(parseExpr()); // parses first argument so we're on the comma now

        while (tokens.get(0).getType() == TokenType.Comma) {
            popLeft(); // gets rid of comma
            arguments.add(parseExpr());
        }

        popLeft(TokenType.CloseP, "Expected closing parentheses after function call.");
        return arguments.toArray(new Expr[0]);
    }

    private Expr parseIncrementExpr() {
        // postfix unary operations (x++, x--)
        Expr left = parseCallOrMemberExpr();
        if (tokens.get(0).getType() == TokenType.Increment) {
            String operator = popLeft().getValue();
            return new UnaryExpr(operator, left);
        }

        return left;
    }

    private Expr parseNegationExpr() {
        // !x
        if (tokens.get(0).getType() == TokenType.Negation) {
            String operator = popLeft().getValue();
            return new UnaryExpr(operator, parseExpr());
        }

        return parseIncrementExpr();
    }

    private Expr parseExponentialExpr() {
        // we always call the function that's above in the order of precedence
        Expr left = parseNegationExpr();

        // basically keeps evaluating the expression and adding it to the right
        while (tokens.get(0).getValue().equals("^")) {
            // now we hit the operator so we want to pop that token
            String operator = popLeft().getValue();
            // the next token is the next expression
            Expr right = parseNegationExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseMultiplicativeExpr() {
        // the exact same as the exponential function just with multiplicative operators
        Expr left = parseExponentialExpr();

        while (tokens.get(0).getValue().equals("/") || tokens.get(0).getValue().equals("//") ||
                tokens.get(0).getValue().equals("*") || tokens.get(0).getValue().equals("%")) {
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

    private Expr parseEquivalenceExpr() {
        Expr left = parseAdditiveExpr();

        while (tokens.get(0).getType() == TokenType.Equivalence) {
            String operator = popLeft().getValue();
            Expr right = parseExpr();

            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseComparitiveExpr() {
        // the exact same as additive function just with comparitive operators
        Expr left = parseEquivalenceExpr();

        while (tokens.get(0).getValue().equals("&") || tokens.get(0).getValue().equals("|")
                || tokens.get(0).getValue().equals("<") || tokens.get(0).getValue().equals("<=")
                || tokens.get(0).getValue().equals(">") || tokens.get(0).getValue().equals(">=")) {
            String operator = popLeft().getValue();
            Expr right = parseEquivalenceExpr();
            left = new BinaryExpr(left, right, operator);
        }

        return left;
    }

    private Expr parseObject() {
        // if we're not at an open brace then we continue up the order of precedence
        if (tokens.get(0).getType() != TokenType.OpenBrace)
            return parseComparitiveExpr();

        popLeft(); // pops open brace
        ArrayList<Property> properties = new ArrayList<Property>();

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

    private Expr parseArray() {
        if (tokens.get(0).getType() != TokenType.OpenBracket)
            return parseObject();

        popLeft(); // pops open bracket
        ArrayList<Expr> elements = new ArrayList<>();

        // nonempty list (not [])
        if (tokens.get(0).getType() != TokenType.CloseBracket) {
            elements.add(parseExpr()); // adds the first element
            while (tokens.get(0).getType() == TokenType.Comma) {
                popLeft(); // gets rid of comma
                elements.add(parseExpr());
            }
        }

        popLeft(TokenType.CloseBracket, "Closing bracket expected after list definition."); // pops closed bracket

        return new Array(elements);
    }

    private Expr parseAssignment() {
        // gets the expression that we're assigning
        Expr left = parseArray();

        while (tokens.get(0).getType() == TokenType.Assignment) {
            String sign = popLeft().getValue(); // pop the assignment sign
            Expr value = parseExpr(); // evaluate the right hand side

            if (sign.equals("=")) {
                // sets left to assignment to allow chains like x = y = z
                left = new Assignment(left, value);
            } else {
                // += -> left = left + value
                // *= -> left = left * value
                // /= -> left = left / value
                left = new Assignment(left, new BinaryExpr(left, value, sign.substring(0, 1)));
            }
        }

        return left;
    }
}
