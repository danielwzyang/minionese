package test;
import java.util.Scanner;

import parser.Parser;
import parser.Program;
import interpreter.BoolVal;
import interpreter.Environment;
import interpreter.Interpreter;
import interpreter.NumberVal;

public class REPL {
    public static void main(String[] args) {
        System.out.println("Type stop to end. Type ? to see variables.");
        Scanner input = new Scanner(System.in);

        // declare testing variables
        Environment globalEnvironment = new Environment();
        globalEnvironment.declareVariable("x", new NumberVal(3), false);
        globalEnvironment.declareVariable("y", new BoolVal(), false);

        while (true) {
            System.out.print("> ");
            String src = input.nextLine();
            if (src.equals("stop")) {
                input.close();
                System.exit(0);
            }

            if (src.equals("?")) {
                globalEnvironment.getVariables().forEach((k, v) -> {
                    System.out.println(k + ": " + v);
                });
                continue;
            }

            Parser parser = new Parser();
            Program program = parser.makeAST(src);
            Interpreter interpreter = new Interpreter();
            System.out.println(interpreter.evaluate(program, globalEnvironment));
        }
    }
}
