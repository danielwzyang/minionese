package interpreter;

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
    
    public String toString() {
        return properties.toString();
    }
}
