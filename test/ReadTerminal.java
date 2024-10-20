package test;
import java.util.Scanner;

import parser.Parser;
import parser.Program;

public class ReadTerminal {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String src = input.nextLine();
            if (src.equals("stop")) 
                System.exit(0);

            Parser parser = new Parser();
            Program program = parser.makeAST(src);
            System.out.println(program);
        }
    }
}
