package runtime.values;

import runtime.ValueType;

public class NullValue extends RuntimeValue {
    private String value;

    public NullValue() {
        super(ValueType.Null);
        value = "null";
    }

    public String getValue() {
        return value;
    }

    public String valueToString() {
        return "" + value;
    }

    public String toString() {
        return value; 
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        return new NullValue();
    }
}
