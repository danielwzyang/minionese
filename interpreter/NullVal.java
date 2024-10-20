package interpreter;

public class NullVal extends RuntimeValue {
    private String value;

    public NullVal() {
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
