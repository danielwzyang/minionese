package runtime.values;

import runtime.ValueType;

public class ReturnValue extends RuntimeValue {
    private RuntimeValue value;

    public ReturnValue() {
        super(ValueType.Return);
    }

    public ReturnValue(RuntimeValue value) {
        super(ValueType.Return);
        this.value = value;
    }

    public RuntimeValue getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        System.err.println("Binary operators are not supported with return values.");
        System.exit(0);
        return new NullValue();
    }
}
