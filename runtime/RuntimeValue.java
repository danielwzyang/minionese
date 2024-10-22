package runtime;

public abstract class RuntimeValue {
    private ValueType type;

    public RuntimeValue(ValueType type) {
        this.type = type;
    }

    public ValueType getType() {
        return type;
    }

    public String toString() {
        return "" + type;
    }

    public abstract String valueToString();

    public abstract RuntimeValue operate(RuntimeValue right, String operator);
}
