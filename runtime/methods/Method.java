package runtime.methods;

import java.util.function.BiFunction;

import runtime.Environment;
import runtime.values.NullValue;
import runtime.values.RuntimeValue;
import runtime.values.ValueType;

public class Method extends RuntimeValue {
    private BiFunction<RuntimeValue[], Environment, RuntimeValue> call;
    private String desc;
    
    public Method(BiFunction<RuntimeValue[], Environment, RuntimeValue> call, String desc) {
        super(ValueType.Method);
        this.call = call;
        this.desc = desc;
    }

    public RuntimeValue operate(RuntimeValue right, String operator) {
        return new NullValue();
    }

    public String toString() {
        return desc;
    }

    public BiFunction<RuntimeValue[], Environment, RuntimeValue> getCall() {
        return call;
    }
}
