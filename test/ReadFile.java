package test;

import java.io.File;
import java.util.Scanner;

import parser.Parser;
import parser.Program;
import runtime.BooleanValue;
import runtime.Environment;
import runtime.NumberValue;

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
            program.evaluate(globalEnvironment);
            System.out.println("File read. REPL starting in this environment.");

            System.out.println("Type stop to end. Type ? to see variables.");
            Scanner input = new Scanner(System.in);

            parser = new Parser();

            while (true) {
                System.out.print("> ");
                src = input.nextLine();
                if (src.equals("stop")) {
                    input.close();
                    System.exit(0);
                }

                if (src.equals("?")) {
                    globalEnvironment.getVariables().forEach((k, v) -> {
                        System.out.println(k + ": " + v.toString());
                    });
                    continue;
                }

                program = parser.makeAST(src);
                program.evaluate(globalEnvironment);
            }
        } catch (Exception error) {
            System.err.println(error);
            System.exit(0);
        }
    }
}
