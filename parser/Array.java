package parser;

import java.util.ArrayList;

import runtime.ArrayValue;
import runtime.Environment;
import runtime.RuntimeValue;

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
