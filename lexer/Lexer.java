package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.tokens.Token;
import lexer.tokens.TokenType;

public class Lexer {
    private static Map<String, TokenType> keywords;
    static {
        // defines keywords that have unique types
        keywords = new HashMap<>();
        keywords.put("la", TokenType.Define);
        keywords.put("fin", TokenType.Final);
        keywords.put("asa", TokenType.If);
        keywords.put("eko", TokenType.Else);
        keywords.put("weebo", TokenType.While);
        keywords.put("para", TokenType.For);
        keywords.put("mef", TokenType.DefineMethod);
        keywords.put("paratu", TokenType.Return);
        keywords.put("stopa", TokenType.Break);
        keywords.put("go", TokenType.Continue);
    }

    public ArrayList<Token> tokenize(String src) {
        ArrayList<Token> tokens = new ArrayList<Token>();

        // set of named capturing groups
        // the order of the groups matters: the groups that are defined first are
        StringBuilder patterns = new StringBuilder();
        patterns.append("(?<Number>-?\\d+(\\.\\d*)?)");
        patterns.append("|(?<Identifier>[a-zA-Z_][\\w]*)");
        patterns.append("|(?<Quote>\")");
        patterns.append("|(?<OpenP>\\()");
        patterns.append("|(?<CloseP>\\))");
        patterns.append("|(?<Equivalence>!=|==)");
        patterns.append("|(?<Assignment>\\+=|-=|\\*=|/=|//=|\\^=|%=|=)");
        patterns.append("|(?<Negation>!)"); 
        patterns.append("|(?<Increment>\\+\\+|--)");
        patterns.append("|(?<BinOp>\\<=|\\>=|\\<|\\>|//|[+\\-*/%\\^&\\|])");
        patterns.append("|(?<Whitespace>[ \\t]+)");
        patterns.append("|(?<OpenBrace>\\{)");
        patterns.append("|(?<CloseBrace>\\})");
        patterns.append("|(?<OpenBracket>\\[)");
        patterns.append("|(?<CloseBracket>\\])");
        patterns.append("|(?<Comma>,)");
        patterns.append("|(?<Colon>:)");
        patterns.append("|(?<Dot>\\.)");
        patterns.append("|(?<String>[.\n])");

        Pattern pattern = Pattern.compile(patterns.toString());
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
            } else if (stringOpen)
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
            } else if (matcher.group("Assignment") != null)
                tokens.add(new Token(matcher.group("Assignment"), TokenType.Assignment));
            else if (matcher.group("Increment") != null)
                tokens.add(new Token(matcher.group("Increment"), TokenType.Increment));
            else if (matcher.group("Negation") != null)
                tokens.add(new Token(matcher.group("Negation"), TokenType.Negation));
            else if (matcher.group("BinOp") != null)
                tokens.add(new Token(matcher.group("BinOp"), TokenType.BinOp));
            else if (matcher.group("Equivalence") != null)
                tokens.add(new Token(matcher.group("Equivalence"), TokenType.Equivalence));
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