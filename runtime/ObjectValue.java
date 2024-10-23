package runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ObjectValue extends RuntimeValue {
    private Map<String, RuntimeValue> properties;

    public ObjectValue() {
        super(ValueType.Object);
        properties = new HashMap<>();
    }

    public RuntimeValue addProperty(String key, RuntimeValue value) {
        properties.put(key, value);
        return value;
    }

    public RuntimeValue getPropertyValue(String key) {
        return properties.get(key);
    }

    public String valueToString() {
        if (properties.size() == 0) return "{}";

        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("{");
        for (String key : properties.keySet()) {
            RuntimeValue value = properties.get(key);
            StringBuilder valueString = new StringBuilder();
            valueString.append("   ");
            valueString.append(key);
            valueString.append(": ");
            if (value.getType() == ValueType.Object) valueString.append(((ObjectValue) value).valueToString(1));
            else valueString.append(value.valueToString());

            stringJoiner.add(valueString);
        }
        stringJoiner.add("}");

        return stringJoiner.toString();
    }

    public String valueToString(int depth) {
        if (properties.size() == 0) return "{}";

        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("{");
        for (String key : properties.keySet()) {
            RuntimeValue value = properties.get(key);
            StringBuilder valueString = new StringBuilder("   ");
            for (int i = 0; i < depth; i++) valueString.append("   ");
            valueString.append(key);
            valueString.append(": ");
            if (value.getType() == ValueType.Object) valueString.append(((ObjectValue) value).valueToString(depth + 1));
            else valueString.append(value.valueToString());

            stringJoiner.add(valueString);
        }
        StringBuilder closingBracket = new StringBuilder();
        for (int i = 0; i < depth; i++) closingBracket.append("   ");
        closingBracket.append("}");

        stringJoiner.add(closingBracket);
        return stringJoiner.toString();
    }
    
    public String toString() {
        return properties.toString();
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        return new NullValue();
    }
}
