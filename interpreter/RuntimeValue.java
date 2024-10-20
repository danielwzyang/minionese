package interpreter;

public class RuntimeValue {
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
}
