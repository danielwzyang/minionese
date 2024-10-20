package interpreter;

public class NumberVal extends RuntimeValue {
    private float value;

    public NumberVal(float value) {
        super(ValueType.Number);
        this.value = value;
    }
}
