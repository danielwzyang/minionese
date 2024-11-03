package parser.stmt;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;

public class Declaration extends Stmt {
    private String identifier;
    private Expr value;
    private boolean isFinal;

    public Declaration(String identifier, boolean isFinal) {
        super(NodeType.Declaration);
        this.identifier = identifier;
        this.isFinal = isFinal;
    }

    public Declaration(String identifier, boolean isFinal, Expr value) {
        super(NodeType.Declaration);
        this.identifier = identifier;
        this.isFinal = isFinal;
        this.value = value;
    }

    public Expr getValue() {
        return value;
    }

    public void setValue(Expr value) {
        this.value = value;
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return "{ type: " + super.type + ", identifier: " + identifier + ", value: " + value + ", isFinal: " + isFinal
                + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        RuntimeValue declaredValue = value == null ? new NullValue() : value.evaluate(environment);

        return environment.declareVariable(identifier, declaredValue, isFinal);
    }
}