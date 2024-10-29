package runtime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import runtime.methods.Method;
import runtime.values.ArrayValue;
import runtime.values.BooleanValue;
import runtime.values.NullValue;
import runtime.values.NumberValue;
import runtime.values.RuntimeValue;
import runtime.values.StringValue;
import runtime.values.ValueType;

public class Environment {
    private Environment parent;
    private Map<String, RuntimeValue> variables; // keeps track of variables and their runtime values
    private Set<String> finals; // keeps track of variables that are finals

    private void initVariables() {
        variables = new HashMap<>();
        finals = new HashSet<>();

        // initialize built-in global variables
        // ex: nool = null

        declareVariable("nool", new NullValue(), true);

        declareVariable("no", new BooleanValue(false), true);
        declareVariable("da", new BooleanValue(true), true);

        declareVariable("nah", new NumberValue(1), true);
        declareVariable("dul", new NumberValue(2), true);
        declareVariable("sae", new NumberValue(3), true);
        declareVariable("pi", new NumberValue(Math.PI), true);

        declareVariable("bello", new Method((args, environment) -> {
            StringJoiner stringJoiner = new StringJoiner(" ");
            for (RuntimeValue arg : args)
                stringJoiner.add(arg.toString());
            System.out.println(stringJoiner.toString());

            return new NullValue();
        }, "prints given arguments"), true);

        declareVariable("veela", new Method((args, environment) -> {
            String date = new SimpleDateFormat("MMMM d, yyyy").format(new Date());

            return new StringValue(date);
        }, "returns string of current date"), true);

        declareVariable("scroot", new Method((args, environment) -> {
            checkStaticArguments("scroot", args, new ValueType[] {ValueType.Number});

            return new NumberValue(Math.sqrt(((NumberValue) args[0]).getValue()));
        }, "returns square root of given number"), true);

        declareVariable("len", new Method((args, environment) -> {
            if (args.length == 0) {
                System.err.println("No arguments provided for length function.");
                System.exit(0);
            }

            if (args[0].getType() == ValueType.String) {
                return new NumberValue(((StringValue) args[0]).getValue().length());
            }

            if (args[0].getType() == ValueType.Array) {
                return new NumberValue(((ArrayValue) args[0]).length());
            }

            System.err.println("The argument provided for length function is not a String.");
            System.exit(0);
            return new NullValue();
        }, "returns length of string"), true);

        declareVariable("bob", new Method((args, environment) -> {
            if (args.length == 0) {
                System.err.println("No arguments provided for pop function.");
                System.exit(0);
            }

            // array to pop from
            if (args[0].getType() != ValueType.Array) {
                System.err.println("The first argument provided for pop function is not an Array.");
                System.exit(0);
            }

            // possible index to pop from
            if (args.length > 1) {
                if (args[1].getType() != ValueType.Number) {
                    System.err.println("The second argument provided for pop function is not a Number.");
                    System.exit(0);
                }

                return ((ArrayValue) args[0]).pop(((NumberValue) args[1]).getValue());
            }

            // no index just pops the last element
            return ((ArrayValue) args[0]).pop();
        }, "pops last element or element at given index of array"), true);

        declareVariable("bobPrima", new Method((args, environment) -> {
            if (args.length == 0) {
                System.err.println("No arguments provided for pop first function.");
                System.exit(0);
            }

            // array to pop from
            if (args[0].getType() != ValueType.Array) {
                System.err.println("The first argument provided for pop first function is not an Array.");
                System.exit(0);
            }

            return ((ArrayValue) args[0]).popFirst();
        }, "pops first element of array"), true);

        declareVariable("hat", new Method((args, environment) -> {
            if (args.length == 0) {
                System.err.println("No arguments provided for push function.");
                System.exit(0);
            }

            // array to push into
            if (args[0].getType() != ValueType.Array) {
                System.err.println("The first argument provided for push function is not an Array.");
                System.exit(0);
            }

            // no value provided
            if (args.length == 1) {
                System.err.println("No value provided (second argument).");
                System.exit(0);
            }

            return ((ArrayValue) args[0]).push(args[1]);
        }, "push element to end of array"), true);

        declareVariable("ponga", new Method((args, environment) -> {
            checkStaticArguments("ponga", args, new ValueType[] {ValueType.Array, ValueType.Number});
            
            return ((ArrayValue) args[0]).insert(args[1], ((NumberValue) args[2]).getValue());
        }, "inserts element into given index of array"), true);

        declareVariable("top", new Method((args, environment) -> {
            checkStaticArguments("top", args, new ValueType[]{ValueType.Array});

            return ((ArrayValue) args[0]).top();
        }, "gets the top element of an array"), false);

        declareVariable("buttom", new Method((args, environment) -> {
            checkStaticArguments("buttom", args, new ValueType[]{ValueType.Array});

            return ((ArrayValue) args[0]).bottom();
        }, "gets the bottom element of an array"), false);

        declareVariable("tinee", new Method((args, environment) -> {
            checkStaticArguments("tinee", args, new ValueType[] {ValueType.String});

            return new StringValue(((StringValue) args[0]).getValue().toLowerCase());
        }, "returns lowercase version of string"), true);

        declareVariable("boma", new Method((args, environment) -> {
            checkStaticArguments("boma", args, new ValueType[] {ValueType.String});

            return new StringValue(((StringValue) args[0]).getValue().toUpperCase());
        }, "returns uppercase version of string"), true);

        declareVariable("even", new Method((args, environment) -> {
            checkStaticArguments("even", args, new ValueType[] {ValueType.Number});

            return new BooleanValue(((NumberValue) args[0]).getValue() % 2 == 0);
        }, "checks whether number is even or not"), true);
        
        declareVariable("odd", new Method((args, environment) -> {
            checkStaticArguments("odd", args, new ValueType[] {ValueType.Number});

            return new BooleanValue(((NumberValue) args[0]).getValue() % 2 != 0);
        }, "checks whether number is odd or not"), true);
    }

    private void checkStaticArguments(String name, RuntimeValue[] arguments, ValueType[] types) {
        // checks whether arguments are valid given a set number of arguments and a set type for each

        if (arguments.length != types.length) {
            System.err
                    .println("Method: " + name + ". Expected " + types.length + " arguments but recieved "
                            + arguments.length + " instead.");
            System.exit(0);
        }

        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].getType() != types[i]) {
                System.err.println("Method: " + name + ". Expected type " + types[i] + " for argument " + (i + 1)
                        + " but recieved type "
                        + arguments[i].getType() + " instead.");
                System.exit(0);
            }
        }
    }

    // for the constructors we have one with no parent and one with a parent

    public Environment() {
        initVariables();
    }

    public Environment(Environment parent) {
        this.parent = parent;
        initVariables();
    }

    public Environment getParent() {
        return parent;
    }

    public void setParent(Environment environment) {
        parent = environment;
    }

    public Map<String, RuntimeValue> getVariables() {
        return variables;
    }

    // to get the variable value we just need to find the scope with the variable
    // and then get it from the map
    public RuntimeValue getVariableValue(String identifier) {
        Environment scope = resolveScope(identifier);
        if (scope != null)
            return scope.getVariables().get(identifier);

        return new NullValue();
    }

    public RuntimeValue declareVariable(String identifier, RuntimeValue value, boolean isFinal) {
        // if the variable is already defined
        if (variables.get(identifier) != null) {
            System.err.println(identifier + " already defined.");
            System.exit(0);
        }

        variables.put(identifier, value);

        if (isFinal)
            finals.add(identifier);

        return value;
    }

    public RuntimeValue assignVariable(String identifier, RuntimeValue value) {
        // we get the environment with the variable
        Environment environment = resolveScope(identifier);

        // if the variable is final then we can't assign something to it
        if (environment.isFinal(identifier)) {
            System.err.println("Cannot assign value to final variable.");
            System.exit(0);
        }

        // if the variable isn't in the scope then it hasn't been defined yet
        if (environment.getVariableValue(identifier) == null) {
            System.err.println(identifier + " not defined yet.");
        } else {
            // to prevent endless loops we have to check if the environment is equal to
            // itself
            if (environment == this) {
                // if it is then we update the value
                variables.put(identifier, value);
            } else {
                // if it's not then we run the assign variable function on the environment with
                // the variable
                environment.assignVariable(identifier, value);
            }
        }

        return value;
    }

    public Environment resolveScope(String identifier) {
        // if the current environment has this variable then the scope is right
        if (variables.get(identifier) != null) {
            return this;
        }

        // if there is no parent then the variable doesn't exist or it exists in a lower
        // scope which cannot be accessed
        if (parent == null) {
            return null;
        }

        // if there is a parent then we want to check if the variable exists in the
        // parent
        return parent.resolveScope(identifier);
    }

    public boolean isFinal(String identifier) {
        return finals.contains(identifier);
    }
}
