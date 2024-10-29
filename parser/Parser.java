package parser;

import java.util.ArrayList;

import lexer.Lexer;
import lexer.tokens.Token;
import lexer.tokens.TokenType;
import parser.expr.Expr;
import parser.literal.Identifier;
import parser.literal.NumericLiteral;
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
    private ExprParser exprParser;

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
        this.exprParser = new ExprParser(tokens);

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
            case TokenType.DefineMethod:
                return parseMethodDeclaration();
            default:
                return exprParser.parseExpr();
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
        Declaration declaration = new Declaration(identifier, isFinal, exprParser.parseExpr());

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
        Expr condition = exprParser.parseExpr(); // evals condition
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
            left = exprParser.parseExpr();
            popLeft(TokenType.Colon, "Colon expected after left bound in for loop.");
        }

        Expr right = exprParser.parseExpr();

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
        return new ReturnStmt(exprParser.parseExpr());
    }

    private Stmt parseBreak() {
        popLeft(); // pops break keyword
        return new BreakStmt();
    }

    private Stmt parseContinue() {
        popLeft(); // pops continue keyword
        return new ContinueStmt();
    }

    private Stmt parseMethodDeclaration() {
        popLeft(); // gets rid of method declaration keyword
        String name = popLeft(TokenType.Identifier, "Identifier expected after method declaration keyword.").getValue();
        Expr[] arguments = exprParser.parseArguments();
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