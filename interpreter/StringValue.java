package interpreter;

public class StringValue extends RuntimeValue {
    private String value;

    public StringValue(String value) {
        super(ValueType.String);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "{ type: " + super.getType() + ", value: " + value + " }";
    }
}
