package runtime;

import java.util.HashMap;
import java.util.Map;

public class ObjectValue extends RuntimeValue {
    private Map<String, RuntimeValue> properties;

    public ObjectValue() {
        super(ValueType.Object);
        properties = new HashMap<>();
    }

    public void addProperty(String key, RuntimeValue value) {
        properties.put(key, value);
    }

    public RuntimeValue getPropertyValue(String key) {
        return properties.get(key);
    }

    public String valueToString() {
        return "Object";
    }
    
    public String toString() {
        return properties.toString();
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        return new NullValue();
    }
}
