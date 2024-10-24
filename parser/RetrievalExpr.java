package parser;

import runtime.Environment;
import runtime.NumberValue;
import runtime.RuntimeValue;
import runtime.StringValue;
import runtime.ValueType;

public class RetrievalExpr extends Expr {
    private Expr string;
    private Expr index;

    public RetrievalExpr(Expr string, Expr index) {
        super(NodeType.SpliceExpr);
        this.string = string;
        this.index = index;
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue value = string.evaluate(environment);
        
        if (value.getType() != ValueType.String) {
            System.err.println("Cannot retrieve from a non-string expression. Expression provided: " + string);
            System.exit(0);
        }

        RuntimeValue indexValue = index.evaluate(environment);

        if (indexValue.getType() != ValueType.Number) {
            System.err.println("Cannot retrieve with a non-number index.");
            System.exit(0);
        }

        int indexInt = (int) ((NumberValue) indexValue).getValue();

        String stringValue = value.valueToString();

        if (indexInt < 0 || indexInt >= stringValue.length()) {
            System.err.println("Index out of bounds while splicing.");
            System.exit(0);
        }

        if (indexInt != ((NumberValue) indexValue).getValue()) {
            System.err.println("Cannot retrieve with non-integer index.");
            System.exit(0);
        }

        return new StringValue(value.valueToString().substring(indexInt, indexInt + 1));
    }
}
