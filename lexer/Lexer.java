package lexer;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static Map<String, TokenType> keywords;
    static {
        // defines the keyword la to use as a variable definition
        keywords = new HashMap<>();
        keywords.put("la", TokenType.Define);
        keywords.put("fin", TokenType.Final);
    }

    public List<Token> tokenize(String src) {
        List<Token> tokens = new ArrayList<Token>();
        
        // set of named capturing groups
        String patterns = "(?<NUMBER>\\d+(\\.\\d*)?)" +
                "|(?<IDENTIFIER>[a-zA-Z_][\\w]*)" +
                "|(?<OPENP>\\()" +
                "|(?<CLOSEP>\\))" +
                "|(?<BINOP>[+\\-*/%\\^])" +
                "|(?<EQUALS>=)" +
                "|(?<WHITESPACE>[ \\t]+)";

        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(src);

        // keeps checking the matcher to go through all the matched tokens
        while (matcher.find()) {
            if (matcher.group("WHITESPACE") != null)
                continue;
            else if (matcher.group("NUMBER") != null)
                tokens.add(new Token(matcher.group("NUMBER"), TokenType.Number));
            else if (matcher.group("IDENTIFIER") != null) {
                TokenType type = keywords.get(matcher.group("IDENTIFIER"));
                
                // if the identifier is in the keywords, then we use the type that's stored
                // ex: identifier = "la", which is the define type not the identifier type
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

        // need an end of file token since we're popping tokens in the parser and we don't want any issues
        tokens.add(new Token("EOF", TokenType.EOF));

        return tokens;
    }
}