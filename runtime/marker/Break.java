package runtime.marker;

import runtime.values.NullValue;
import runtime.values.RuntimeValue;
import runtime.values.ValueType;

public class Break extends RuntimeValue {
    public Break() {
        super(ValueType.Break);
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
