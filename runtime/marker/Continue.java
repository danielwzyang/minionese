package runtime.marker;

import runtime.values.NullValue;
import runtime.values.RuntimeValue;
import runtime.values.ValueType;

public class Continue extends RuntimeValue {
    public Continue() {
        super(ValueType.Continue);
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
