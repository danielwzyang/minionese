package test;
import java.util.Scanner;

import parser.Parser;
import parser.Program;
import interpreter.Interpreter;

public class ReadTerminal {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String src = input.nextLine();
            if (src.equals("stop")) {
                input.close();
                System.exit(0);
            }

            Parser parser = new Parser();
            Program program = parser.makeAST(src);

            Interpreter interpreter = new Interpreter();
            System.out.println(interpreter.evaluate(program));
        }
    }
}
