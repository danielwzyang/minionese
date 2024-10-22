package runtime;

public class NullValue extends RuntimeValue {
    private String value;

    public NullValue() {
        super(ValueType.Null);
        value = "null";
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "{ type: " + super.getType() + ", value: " + value + "}"; 
    }
}
