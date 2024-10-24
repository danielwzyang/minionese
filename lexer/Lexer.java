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
        // defines keywords that have unique types
        keywords = new HashMap<>();
        keywords.put("la", TokenType.Define);
        keywords.put("fin", TokenType.Final);
        keywords.put("papoy", TokenType.If);
    }

    public List<Token> tokenize(String src) {
        List<Token> tokens = new ArrayList<Token>();

        // set of named capturing groups
        String patterns = "(?<Number>-?\\d+(\\.\\d*)?)" +
                "|(?<Identifier>[a-zA-Z_][\\w]*)" +
                "|(?<Quote>\")" +
                "|(?<OpenP>\\()" +
                "|(?<CloseP>\\))" +
                "|(?<BinOp>==|\\<=|\\>=|\\<|\\>|[+\\-*/%\\^&\\|])" +
                "|(?<Equals>=)" +
                "|(?<Whitespace>[ \\t]+)" +
                "|(?<OpenBrace>\\{)" +
                "|(?<CloseBrace>\\})" +
                "|(?<OpenBracket>\\[)" +
                "|(?<CloseBracket>\\])" +
                "|(?<Comma>,)" +
                "|(?<Colon>:)" +
                "|(?<Dot>\\.)" +
                "|(?<String>[.\n])";

        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(src);

        boolean stringOpen = false;
        StringBuilder string = new StringBuilder();

        // keeps checking the matcher to go through all the matched tokens
        while (matcher.find()) {
            if (matcher.group("Quote") != null) {
                stringOpen = !stringOpen;
                if (!stringOpen) {
                    tokens.add(new Token(string.toString(), TokenType.String));
                    string.setLength(0);
                }
            }
            else if (stringOpen)
                string.append(matcher.group());
            else if (matcher.group("Whitespace") != null)
                continue;
            else if (matcher.group("Number") != null)
                tokens.add(new Token(matcher.group("Number"), TokenType.Number));
            else if (matcher.group("Identifier") != null) {
                TokenType type = keywords.get(matcher.group("Identifier"));

                // if the identifier is in the keywords, then we use the type that's stored
                // ex: identifier = "la", which is the define type not the identifier type
                tokens.add(new Token(matcher.group("Identifier"), type == null ? TokenType.Identifier : type));
            } else if (matcher.group("BinOp") != null)
                tokens.add(new Token(matcher.group("BinOp"), TokenType.BinOp));
            else if (matcher.group("Equals") != null)
                tokens.add(new Token(matcher.group("Equals"), TokenType.Equals));
            else if (matcher.group("OpenP") != null)
                tokens.add(new Token(matcher.group("OpenP"), TokenType.OpenP));
            else if (matcher.group("CloseP") != null)
                tokens.add(new Token(matcher.group("CloseP"), TokenType.CloseP));
            else if (matcher.group("OpenBrace") != null)
                tokens.add(new Token(matcher.group("OpenBrace"), TokenType.OpenBrace));
            else if (matcher.group("CloseBrace") != null)
                tokens.add(new Token(matcher.group("CloseBrace"), TokenType.CloseBrace));
            else if (matcher.group("OpenBracket") != null)
                tokens.add(new Token(matcher.group("OpenBracket"), TokenType.OpenBracket));
            else if (matcher.group("CloseBracket") != null)
                tokens.add(new Token(matcher.group("CloseBracket"), TokenType.CloseBracket));
            else if (matcher.group("Comma") != null)
                tokens.add(new Token(matcher.group("Comma"), TokenType.Comma));
            else if (matcher.group("Colon") != null)
                tokens.add(new Token(matcher.group("Colon"), TokenType.Colon));
            else if (matcher.group("Dot") != null)
                tokens.add(new Token(matcher.group("Dot"), TokenType.Dot));
        }

        // need an end of file token since we're popping tokens in the parser and we
        // don't want any issues
        tokens.add(new Token("EOF", TokenType.EOF));

        return tokens;
    }
}