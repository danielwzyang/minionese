package parser.literal;

import java.util.ArrayList;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.values.ArrayValue;
import runtime.values.RuntimeValue;

public class Array extends Expr {
    ArrayList<Expr> elements;

    public Array(ArrayList<Expr> elements) {
        super(NodeType.Array);
        this.elements = elements;
    }

    public RuntimeValue evaluate(Environment environment) {
        ArrayList<RuntimeValue> values = new ArrayList<>();
        
        for (Expr expr : elements) {
            values.add(expr.evaluate(environment));
        }

        return new ArrayValue(values);
    }
}
