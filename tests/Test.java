package tests;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import lexer.Lexer;
import lexer.Token;

public class Test {
    public static void main(String[] args) {
        try {
            File file = new File(args[0]);
            String src = "";
            Scanner reader = new Scanner(file);
            reader.useDelimiter("\\Z");
            src += reader.next();
            reader.close();

            Lexer tokenizer = new Lexer();
            Vector<Token> tokens = tokenizer.tokenize(src);
            for (Token token : tokens) {
                System.out.println(token);
            }
        } catch (Exception error) {
            System.err.println(error);
            System.exit(0);
        }
    }
}
