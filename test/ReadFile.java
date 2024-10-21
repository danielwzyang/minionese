package test;
import java.io.File;
import java.util.Scanner;

import interpreter.BooleanValue;
import interpreter.Environment;
import interpreter.Interpreter;
import interpreter.NumberValue;
import parser.Parser;
import parser.Program;

public class ReadFile {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.err.println("No file name provided. Example call: java test/ReadFile.java test/test.minion");
                System.exit(0);
            }

            File file = new File(args[0]);
            String src = "";
            Scanner reader = new Scanner(file);
            reader.useDelimiter("\\Z");
            src += reader.next();
            reader.close();

            // declare testing variables
            Environment globalEnvironment = new Environment();
            globalEnvironment.declareVariable("x", new NumberValue(3), false);
            globalEnvironment.declareVariable("y", new BooleanValue(), false);
            
            Parser parser = new Parser();
            Program program = parser.makeAST(src);
            Interpreter interpreter = new Interpreter();
            System.out.println(interpreter.evaluate(program, globalEnvironment));
        } catch (Exception error) {
            System.err.println(error);
            System.exit(0);
        }
    }
}
