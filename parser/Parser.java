package parser;

import java.util.ArrayList;

import lexer.Lexer;
import lexer.tokens.Token;
import lexer.tokens.TokenType;
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
import parser.stmt.Assignment;
import parser.stmt.BreakStmt;
import parser.stmt.ContinueStmt;
import parser.stmt.Declaration;
import parser.stmt.ForStmt;
import parser.stmt.IfStmt;
import parser.stmt.MethodDeclaration;
import parser.stmt.Program;
import parser.stmt.ReturnStmt;
import parser.stmt.Stmt;
import parser.stmt.WhileStmt;

public class Parser {
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
            case TokenType.If:
                return parseConditional();
            case TokenType.While:
                return parseConditional();
            case TokenType.For:
                return parseFor();
            case TokenType.Return:
                return parseReturn();
            case TokenType.Break:
                return parseBreak();
            case TokenType.Continue:
                return parseContinue();
            default:
                return parseExpr();
        }
    }

    private Declaration parseDeclaration() {
        Boolean isFinal = popLeft().getType() == TokenType.Final;
        String identifier = popLeft(TokenType.Identifier, "Identifier expected.").getValue();

        // if there's no value provided
        if (tokens.get(0).getType() != TokenType.Assignment) {
            // if the token is final then this doesn't work since we can't define a final
            // with no value
            if (isFinal) {
                System.err.println("Value expected for final definition of " + identifier + ".");
                System.exit(0);
            }

            return new Declaration(identifier, isFinal);
        }

        String sign = popLeft().getValue(); // pops equal sign
        if (!sign.equals("=")) {
            System.err.println("Expected equals sign for declaration.");
            System.exit(0);
        }

        // after the equals sign is the expression for the value
        Declaration declaration = new Declaration(identifier, isFinal, parseExpr());

        return declaration;
    }

    private ArrayList<Stmt> parseStatementBody() {
        ArrayList<Stmt> body = new ArrayList<>();

        // if { ... } (multiple statements)
        if (tokens.get(0).getType() == TokenType.OpenBrace) {
            popLeft(); // gets rid of open brace
            while (tokens.get(0).getType() != TokenType.CloseBrace) {
                if (tokens.get(0).getType() == TokenType.EOF) {
                    System.err.println("Closing brace expected to close statement body.");
                    System.exit(0);
                }

                body.add(parseStatement());
            }

            popLeft(); // gets rid of closed brace
        } else {
            // if ... (one statement)
            if (tokens.get(0).getType() == TokenType.EOF) {
                System.err.println("Statement expected for statement body.");
                System.exit(0);
            }

            body.add(parseStatement());
        }

        return body;
    }

    private Stmt parseConditional() {
        Token identifier = popLeft(); // gets rid of if / while identifier
        Expr condition = parseExpr(); // evals condition
        ArrayList<Stmt> body = parseStatementBody();

        // if else
        if (identifier.getType() == TokenType.If && tokens.get(0).getType() == TokenType.Else) {
            popLeft(); // gets rid of else
            ArrayList<Stmt> elseBody = parseStatementBody();

            return new IfStmt(condition, body, elseBody);
        }

        return identifier.getType() == TokenType.If ? new IfStmt(condition, body, new ArrayList<>())
                : new WhileStmt(condition, body);
    }

    private Stmt parseFor() {
        // format: for i 3:10
        popLeft(); // pops for loop identifier
        String identifier = popLeft(TokenType.Identifier, "Identifier expected after for loop.").getValue();

        Expr left;
        // no left bound provided; ex: para i :10
        if (tokens.get(0).getType() == TokenType.Colon) {
            popLeft(); // gets rid of colon
            left = new NumericLiteral(0);
        } else {
            left = parseExpr();
            popLeft(TokenType.Colon, "Colon expected after left bound in for loop.");
        }

        Expr right = parseExpr();

        ArrayList<Stmt> body = new ArrayList<>();
        // for i 3:10 { ... }
        if (tokens.get(0).getType() == TokenType.OpenBrace) {
            popLeft(); // gets rid of open brace

            while (tokens.get(0).getType() != TokenType.CloseBrace) {
                if (tokens.get(0).getType() == TokenType.EOF) {
                    System.err.println("Closing brace expected to close if statement.");
                    System.exit(0);
                }

                body.add(parseStatement());
            }

            popLeft(); // pops close brace
        } else {
            // for i 3:10 ...

            if (tokens.get(0).getType() == TokenType.EOF) {
                System.err.println("Statement expected after condition for if statement.");
                System.exit(0);
            }

            body.add(parseStatement());
        }

        return new ForStmt(new Identifier(identifier), left, right, body);
    }

    private Stmt parseReturn() {
        popLeft(); // pops return keyword
        return new ReturnStmt(parseExpr());
    }

    private Stmt parseBreak() {
        popLeft(); // pops break keyword
        return new BreakStmt();
    }

    private Stmt parseContinue() {
        popLeft(); // pops continue keyword
        return new ContinueStmt();
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
     * method declaration
     */

    private Expr parseExpr() {
        // this always calls the one in the bottom of the order of precedence
        return parseMethodDeclaration();
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

    private Expr[] parseArguments() {
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
            Expr right = parseAdditiveExpr();

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

    private Expr parseMethodDeclaration() {
        if (tokens.get(0).getType() != TokenType.DefineMethod)
            return parseAssignment();

        popLeft(); // gets rid of method declaration keyword
        String name = popLeft(TokenType.Identifier, "Identifier expected after method declaration keyword.").getValue();
        Expr[] arguments = parseArguments();
        String[] params = new String[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].getType() != NodeType.Identifier) {
                System.err.println("Parameter provided for method declaration is not an identifier.");
                System.exit(0);
            }
            params[i] = ((Identifier) arguments[i]).getName();
        }
        ArrayList<Stmt> body = parseStatementBody();

        return new MethodDeclaration(name, params, body);
    }
}