package parser.stmt;

import parser.NodeType;
import runtime.Environment;
import runtime.marker.Break;
import runtime.values.RuntimeValue;

public class BreakStmt extends Stmt {
    public BreakStmt() {
        super(NodeType.Break);
    }

    public RuntimeValue evaluate(Environment environment) {
        return new Break();
    }
}
