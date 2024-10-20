package interpreter;

public class BoolVal extends RuntimeValue {
    private boolean value;

    public BoolVal() {
        super(ValueType.Boolean);
        value = false;
    }

    public BoolVal(boolean value) {
        super(ValueType.Boolean);
        this.value = value;
    }

    public String toString() {
        return "{ type: " + super.getType() + ", value: " + value + " }";
    }
}
