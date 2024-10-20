package interpreter;

public class NullVal extends RuntimeValue {
    private String value;

    public NullVal() {
        super(ValueType.Null);
        value = "null";
    }
}
