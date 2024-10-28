package parser.stmt;

import parser.NodeType;
import runtime.Environment;
import runtime.marker.Continue;
import runtime.values.RuntimeValue;

public class ContinueStmt extends Stmt {
    
    public ContinueStmt() {
        super(NodeType.Continue);
    }

    public RuntimeValue evaluate(Environment environment) {
        return new Continue();
    }
}
