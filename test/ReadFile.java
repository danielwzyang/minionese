package test;
import java.io.File;
import java.util.Scanner;

import parser.Parser;
import parser.Program;

public class ReadFile {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.err.println("No file name provided.");
                System.exit(0);
            }

            File file = new File(args[0]);
            String src = "";
            Scanner reader = new Scanner(file);
            reader.useDelimiter("\\Z");
            src += reader.next();
            reader.close();

            Parser parser = new Parser();
            Program program = parser.makeAST(src);
            System.out.println(program);
        } catch (Exception error) {
            System.err.println(error);
            System.exit(0);
        }
    }
}
