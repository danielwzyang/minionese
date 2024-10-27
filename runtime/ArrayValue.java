package runtime;

import java.util.ArrayList;

public class ArrayValue extends RuntimeValue {
    ArrayList<RuntimeValue> elements;

    public ArrayValue(ArrayList<RuntimeValue> elements) {
        super(ValueType.Array);
        this.elements = elements;
    }

    public String toString() {
        ArrayList<String> elementStrings = new ArrayList<>();
        for (RuntimeValue ele : elements) {
            elementStrings.add(ele.toString());
        }

        return elementStrings.toString();
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
