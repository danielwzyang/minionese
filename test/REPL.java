package test;
import java.util.Scanner;

import parser.Parser;
import parser.Program;
import runtime.BooleanValue;
import runtime.Environment;
import runtime.NumberValue;
import runtime.StringValue;

public class REPL {
    public static void main(String[] args) {
        System.out.println("Type stop to end. Type ? to see variables.");
        Scanner input = new Scanner(System.in);

        // declare testing variables
        Environment globalEnvironment = new Environment();
        globalEnvironment.declareVariable("x", new NumberValue(3), false);
        globalEnvironment.declareVariable("y", new BooleanValue(), false);
        globalEnvironment.declareVariable("bello", new StringValue("hello"), false);

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
            System.out.println(program.evaluate(globalEnvironment));
        }
    }
}
