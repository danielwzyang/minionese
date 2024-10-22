package runtime;

public class NumberValue extends RuntimeValue {
    private double value;

    public NumberValue() {
        super(ValueType.Number);
        value = 0;
    }

    public NumberValue(double value) {
        super(ValueType.Number);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "{ type: " + super.getType() + ", value: " + value + "}"; 
    }
}
