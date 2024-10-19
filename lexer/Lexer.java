package lexer;

import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("la", TokenType.Define);
    }

    public Vector<Token> tokenize(String src) {
        Vector<Token> tokens = new Vector<Token>();
        String patterns = "(?<NUMBER>\\d+(\\.\\d*)?)" +
                "|(?<IDENTIFIER>[a-zA-Z_][\\w]*)" +
                "|(?<OPENP>\\()" +
                "|(?<CLOSEP>\\))" +
                "|(?<BINOP>[+\\-*/])" +
                "|(?<EQUALS>=)" +
                "|(?<WHITESPACE>[ \\t]+)";

        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(src);

        while (matcher.find()) {
            if (matcher.group("WHITESPACE") != null)
                continue;
            else if (matcher.group("NUMBER") != null)
                tokens.add(new Token(matcher.group("NUMBER"), TokenType.Number));
            else if (matcher.group("IDENTIFIER") != null) {
                TokenType type = keywords.get(matcher.group("IDENTIFIER"));
                tokens.add(new Token(matcher.group("IDENTIFIER"), type == null ? TokenType.Identifier : type));
            } else if (matcher.group("OPENP") != null)
                tokens.add(new Token(matcher.group("OPENP"), TokenType.OpenP));
            else if (matcher.group("CLOSEP") != null)
                tokens.add(new Token(matcher.group("CLOSEP"), TokenType.CloseP));
            else if (matcher.group("BINOP") != null)
                tokens.add(new Token(matcher.group("BINOP"), TokenType.BinOp));
            else if (matcher.group("EQUALS") != null)
                tokens.add(new Token(matcher.group("EQUALS"), TokenType.Equals));
        }

        tokens.add(new Token("EOF", TokenType.EOF));

        return tokens;
    }
}