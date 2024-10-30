package runtime.values;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import runtime.ValueType;

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

    private String depthString(int depth) {
        if (properties.size() == 0) return "{}";

        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("{");
        for (String key : properties.keySet()) {
            RuntimeValue value = properties.get(key);
            StringBuilder valueString = new StringBuilder("   ");
            for (int i = 0; i < depth; i++) valueString.append("   ");
            valueString.append(key);
            valueString.append(": ");
            if (value.getType() == ValueType.Object) valueString.append(((ObjectValue) value).depthString(depth + 1));
            else valueString.append(value.toString());

            stringJoiner.add(valueString);
        }
        StringBuilder closingBracket = new StringBuilder();
        for (int i = 0; i < depth; i++) closingBracket.append("   ");
        closingBracket.append("}");

        stringJoiner.add(closingBracket);
        return stringJoiner.toString();
    }
    
    public String toString() {
        if (properties.size() == 0) return "{}";

        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("{");
        for (String key : properties.keySet()) {
            RuntimeValue value = properties.get(key);
            StringBuilder valueString = new StringBuilder();
            valueString.append("   ");
            valueString.append(key);
            valueString.append(": ");
            if (value.getType() == ValueType.Object) valueString.append(((ObjectValue) value).depthString(1));
            else valueString.append(value.toString());

            stringJoiner.add(valueString);
        }
        stringJoiner.add("}");

        return stringJoiner.toString();
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        System.err.println("Binary operators are not supported with objects.");
        System.exit(0);
        return new NullValue();
    }
}
