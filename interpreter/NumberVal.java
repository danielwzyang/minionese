package interpreter;

public class NumberVal extends RuntimeValue {
    private double value;

    public NumberVal() {
        super(ValueType.Number);
        value = 0;
    }

    public NumberVal(double value) {
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
