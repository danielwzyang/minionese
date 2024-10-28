package runtime.values;

import java.util.ArrayList;

import runtime.Environment;

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

    public double length() {
        return elements.size();
    }

    public RuntimeValue pop() {
        return elements.removeLast();
    }

    public RuntimeValue pop(double index) {
        if ((int) index != index) {
            System.err.println("Cannot pop array element at non-integer index.");
            System.exit(0);
        }

        if (index < 0 || index >= elements.size()) {
            System.err.println("Index out of bounds while popping array element.");
            System.exit(0);
        }

        RuntimeValue popped = elements.get((int) index);
        elements.remove((int) index);

        return popped;
    }

    public RuntimeValue popFirst() {
        return elements.removeFirst();
    }

    public RuntimeValue push(RuntimeValue value) {
        elements.add(value);
        return value;
    }

    public RuntimeValue insert(RuntimeValue value, double index) {
        if ((int) index != index) {
            System.err.println("Cannot insert array element at non-integer index.");
            System.exit(0);
        }

        if (index < 0 || index >= elements.size()) {
            System.err.println("Index out of bounds while inserting array element.");
            System.exit(0);
        }

        elements.add((int) index, value);
        return value;
    }

    public RuntimeValue get(NumberValue index) {
        double i = index.getValue();
        if ((int) i != i) {
            System.err.println("Cannot retrieve array element at non-integer index.");
            System.exit(0);
        }

        if (i < 0 || i >= elements.size()) {
            System.err.println("Index out of bounds while retrieving array element.");
            System.exit(0);
        }

        return elements.get((int) i);
    }

    public RuntimeValue splice(RuntimeValue left, RuntimeValue right, Environment environment) {
        if (left.getType() != ValueType.Number || right.getType() != ValueType.Number) {
            System.err.println("Cannot splice with non-number indices.");
            System.exit(0);
        }

        int leftBound = (int) ((NumberValue) left).getValue();
        int rightBound = (int) ((NumberValue) right).getValue();

        if (leftBound != ((NumberValue) left).getValue() || rightBound != ((NumberValue) right).getValue()) {
            System.err.println("Cannot splice with non-integer indices.");
            System.exit(0);
        }

        if (leftBound < 0 || rightBound > elements.size()) {
            System.err.println("Index out of bounds while splicing.");
            System.exit(0);
        }

        return new ArrayValue(new ArrayList<>(elements.subList(leftBound, rightBound)));
    }

    public RuntimeValue operate(RuntimeValue value, String operator) {
        return new NullValue();
    }
}
