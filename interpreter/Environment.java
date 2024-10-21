package interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class Environment {
    private Environment parent;
    private Map<String, RuntimeValue> variables; // keeps track of variables and their runtime values
    private Set<String> finals; // keeps track of variables that are finals

    private void initVariables() {
        variables = new HashMap<>();
        finals = new HashSet<>();

        // initialize built-in global variables
        // ex: nool = null

        declareVariable("nool", new NullVal(), true);
        declareVariable("no", new BoolVal(false), true);
        declareVariable("da", new BoolVal(true), true);

        declareVariable("nah", new NumberVal(1), true);
        declareVariable("dul", new NumberVal(2), true);
        declareVariable("sae", new NumberVal(3), true);
        declareVariable("pi", new NumberVal(Math.PI), true);
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
    
    public Map<String, RuntimeValue> getVariables() {
        return variables;
    }

    // to get the variable value we just need to find the scope with the variable and then get it from the map
    public RuntimeValue getVariableValue(String identifier) {
        return resolveScope(identifier).getVariables().get(identifier);
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
            // to prevent endless loops we have to check if the environment is equal to itself
            if (environment == this) {
                // if it is then we update the value
                variables.put(identifier, value);
            }
            else {
                // if it's not then we run the assign variable function on the environment with the variable
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

        // if there is no parent then the variable doesn't exist or it exists in a lower scope which cannot be accessed
        if (parent == null) {
            System.err.println("Cannot resolve " + identifier + ".");
        }

        // if there is a parent then we want to check if the variable exists in the parent
        return parent.resolveScope(identifier);
    }

    public Boolean isFinal(String identifier) {
        return finals.contains(identifier);
    }
}
