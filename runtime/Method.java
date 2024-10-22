package runtime;

import java.util.function.BiFunction;

public class Method extends RuntimeValue {
    private BiFunction<RuntimeValue[], Environment, RuntimeValue> call;
    
    public Method(BiFunction<RuntimeValue[], Environment, RuntimeValue> call) {
        super(ValueType.Method);
        this.call = call;
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        return new NullValue();
    }

    public String valueToString() {
        return "Method";
    }

    public BiFunction<RuntimeValue[], Environment, RuntimeValue> getCall() {
        return call;
    }
}
