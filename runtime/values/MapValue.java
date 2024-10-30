package runtime.values;

import java.util.HashMap;

import runtime.ValueType;

public class MapValue extends RuntimeValue {
    private HashMap<String, RuntimeValue> values;

    public MapValue() {
        super(ValueType.Map);
        this.values = new HashMap<>();
    }

    public RuntimeValue get(RuntimeValue key) {
        return values.get(key.toString());
    }

    public RuntimeValue put(RuntimeValue key, RuntimeValue value) {
        return values.put(key.toString(), value);
    }

    public RuntimeValue remove(RuntimeValue key) {
        return values.remove(key.toString());
    }

    public boolean containsKey(RuntimeValue key) {
        return values.containsKey(key.toString());
    }

    public String toString() {
        return values.toString();
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
