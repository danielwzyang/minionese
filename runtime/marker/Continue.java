package runtime.marker;

import runtime.ValueType;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;

public class Continue extends RuntimeValue {
    public Continue() {
        super(ValueType.Continue);
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
