package runtime;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;

import runtime.methods.Method;
import runtime.values.ArrayValue;
import runtime.values.BooleanValue;
import runtime.values.MapValue;
import runtime.values.NullValue;
import runtime.values.NumberValue;
import runtime.values.RuntimeValue;
import runtime.values.StringValue;

public class NativeMethods {
    Environment environment;

    public NativeMethods(Environment environment) {
        this.environment = environment;
    }

    private void declareMethod(String name, RuntimeValue method) {
        environment.declareVariable(name, method, true);
    }

    private void checkArguments(String name, RuntimeValue[] arguments, ValueType[][] types) {
        checkDynamicArguments(name, arguments, types, types.length, types.length);
    }

    private void checkArguments(String name, RuntimeValue[] arguments, ValueType[][] types, int left, int right) {
        checkDynamicArguments(name, arguments, types, left, right);
    }

    private void checkDynamicArguments(String name, RuntimeValue[] arguments, ValueType[][] types, int left,
            int right) {
        if (arguments.length < left || arguments.length > right) {
            System.err
                    .println("Method: " + name + ". Expected " +
                            (left == right ? left : " between " + left + " and " + right) + " argument(s) but recieved "
                            + arguments.length + " instead.");
            System.exit(0);
        }

        for (int i = 0; i < arguments.length; i++) {
            if (types[i].length > 0 && !Arrays.asList(types[i]).contains(arguments[i].getType())) {
                System.err.println("Method: " + name + ". Expected type(s) " + types[i] + " for argument " + (i + 1)
                        + " but recieved type "
                        + arguments[i].getType() + " instead.");
                System.exit(0);
            }
        }
    }

    public void declareMethods() {
        // misc
        declareMethod("bello", new Method((args, environment) -> {
            StringJoiner stringJoiner = new StringJoiner(" ");
            for (RuntimeValue arg : args)
                stringJoiner.add(arg.toString());
            System.out.println(stringJoiner.toString());

            return new NullValue();
        }, "prints given arguments"));

        declareMethod("veela", new Method((args, environment) -> {
            checkArguments("veela", args, new ValueType[0][]);
            String date = new SimpleDateFormat("MMMM d, yyyy").format(new Date());

            return new StringValue(date);
        }, "returns string of current date"));



        // math
        declareMethod("scroot", new Method((args, environment) -> {
            checkArguments("scroot", args, new ValueType[][] { { ValueType.Number } });

            return new NumberValue(Math.sqrt(((NumberValue) args[0]).getValue()));
        }, "returns square root of given number"));        

        declareMethod("even", new Method((args, environment) -> {
            checkArguments("even", args, new ValueType[][] { { ValueType.Number } });

            return new BooleanValue(((NumberValue) args[0]).getValue() % 2 == 0);
        }, "checks whether number is even or not"));

        declareMethod("odd", new Method((args, environment) -> {
            checkArguments("odd", args, new ValueType[][] { { ValueType.Number } });

            return new BooleanValue(((NumberValue) args[0]).getValue() % 2 != 0);
        }, "checks whether number is odd or not"));



        // arrays
        declareMethod("len", new Method((args, environment) -> {
            checkArguments("len", args, new ValueType[][] { { ValueType.String, ValueType.Array } });

            // if the arg is a string
            if (args[0].getType() == ValueType.String) {
                return new NumberValue(((StringValue) args[0]).getValue().length());
            }

            // if the arg is an array
            return new NumberValue(((ArrayValue) args[0]).length());
        }, "returns length of string"));

        declareMethod("bob", new Method((args, environment) -> {
            checkArguments("bob", args, new ValueType[][] { { ValueType.Array }, { ValueType.Number } }, 1, 2);

            // possible index to pop from
            if (args.length > 1)
                return ((ArrayValue) args[0]).pop(((NumberValue) args[1]).getValue());

            // no index just pops the last element
            return ((ArrayValue) args[0]).pop();
        }, "pops last element or element at given index of array"));

        declareMethod("bobPrima", new Method((args, environment) -> {
            checkArguments("bobPrima", args, new ValueType[][] { { ValueType.Array } });

            return ((ArrayValue) args[0]).popFirst();
        }, "pops first element of array"));

        declareMethod("hat", new Method((args, environment) -> {
            checkArguments("hat", args, new ValueType[][] { { ValueType.Array }, {}, { ValueType.Number } }, 2, 3);
            
            if (args.length == 2)
                return ((ArrayValue) args[0]).push(args[1]);
            
            return ((ArrayValue) args[0]).insert(args[1], ((NumberValue) args[2]).getValue());
        }, "push element to end of array or inserts element into given index"));

        declareMethod("top", new Method((args, environment) -> {
            checkArguments("top", args, new ValueType[][] { { ValueType.Array } });

            return ((ArrayValue) args[0]).top();
        }, "gets the top element of an array"));

        declareMethod("buttom", new Method((args, environment) -> {
            checkArguments("buttom", args, new ValueType[][] { { ValueType.Array } });

            return ((ArrayValue) args[0]).bottom();
        }, "gets the bottom element of an array"));



        // strings
        declareMethod("tinee", new Method((args, environment) -> {
            checkArguments("tinee", args, new ValueType[][] { { ValueType.String } });

            return new StringValue(((StringValue) args[0]).getValue().toLowerCase());
        }, "returns lowercase version of string"));

        declareMethod("boma", new Method((args, environment) -> {
            checkArguments("boma", args, new ValueType[][] { { ValueType.String } });

            return new StringValue(((StringValue) args[0]).getValue().toUpperCase());
        }, "returns uppercase version of string"));

        

        // maps
        declareMethod("mapa", new Method((args, environment) -> {
            checkArguments("mapa", args, new ValueType[0][]);

            return new MapValue();
        }, "returns a new map object"));

        declareMethod("nob", new Method((args, environment) -> {
            checkArguments("nob", args, new ValueType[][]{ {ValueType.Map}, {} });
            
            RuntimeValue value = ((MapValue) args[0]).get(args[1]);
            return value != null ? value : new NullValue();
        }, "returns the value associated with a key in the map (returns null if it doesn't exist)"));

        declareMethod("ponga", new Method((args, environment) -> {
            checkArguments("ponga", args, new ValueType[][] { {ValueType.Map}, {}, {}});

            RuntimeValue value = ((MapValue) args[0]).put(args[1], args[2]);
            return value != null ? value : new NullValue();
        }, "puts in a key value pair into a map"));

        declareMethod("yoink", new Method((args, environment) -> {
            checkArguments("yoink", args, new ValueType[][] { {ValueType.Map}, {}});

            RuntimeValue value = ((MapValue) args[0]).remove(args[1]);
            return value != null ? value : new NullValue();
        }, "removes a key value pair from the map"));

        declareMethod("has", new Method((args, environment) -> {
            checkArguments("has", args, new ValueType[][] { {ValueType.Map}, {}});

            return new BooleanValue(((MapValue) args[0]).containsKey(args[1]));
        }, "returns if a map contains the given key"));
    }
}
