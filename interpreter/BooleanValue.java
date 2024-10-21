package interpreter;

public class BooleanValue extends RuntimeValue {
    private boolean value;

    public BooleanValue() {
        super(ValueType.Boolean);
        value = false;
    }

    public BooleanValue(boolean value) {
        super(ValueType.Boolean);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public String toString() {
        return "{ type: " + super.getType() + ", value: " + value + " }";
    }
}
