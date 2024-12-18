package test;
import java.util.Scanner;

import parser.Parser;
import parser.stmt.Program;
import runtime.Environment;
import runtime.values.NumberValue;

public class REPL {
    public static void main(String[] args) {
        System.out.println("Type stop to end. Type ? to see variables.");
        Scanner input = new Scanner(System.in);

        // declare testing variables
        Environment globalEnvironment = new Environment();
        globalEnvironment.declareVariable("min", new NumberValue(Integer.MIN_VALUE), false);
        globalEnvironment.declareVariable("max", new NumberValue(Integer.MAX_VALUE), false);
        Parser parser = new Parser();

        while (true) {
            System.out.print("> ");
            String src = input.nextLine();
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
            
            Program program = parser.makeAST(src);
            program.evaluate(globalEnvironment);
        }
    }
}
