package interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class Environment {
    private Environment parent;
    private Map<String, RuntimeValue> variables;
    private Set<String> finals;

    private void initVariables() {
        variables = new HashMap<>();
        finals = new HashSet<>();

        // initialize built-in global variables
        // ex: nool = null

        declareVariable("nool", new NullVal(), true);

        declareVariable("nah", new NumberVal(1), true);
        declareVariable("dul", new NumberVal(2), true);
        declareVariable("sae", new NumberVal(3), true);

        declareVariable("no", new BoolVal(false), true);
        declareVariable("da", new BoolVal(true), true);
    }

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

    public RuntimeValue getVariableValue(String identifier) {
        return resolveScope(identifier).getVariables().get(identifier);
    }

    public RuntimeValue declareVariable(String identifier, RuntimeValue value, boolean isFinal) {
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
        Environment environment = resolveScope(identifier);

        if (environment.isFinal(identifier)) {
            System.err.println("Cannot assign value to final variable.");
            System.exit(0);
        }

        if (environment.getVariableValue(identifier) == null) {
            System.err.println(identifier + " not defined yet.");
        } else {
            if (environment == this) {
                variables.put(identifier, value);
            }
            else {
                environment.assignVariable(identifier, value);
            }
        }

        return value;
    }

    public Environment resolveScope(String identifier) {
        if (variables.get(identifier) != null) {
            return this;
        }

        if (parent == null) {
            System.err.println("Cannot resolve " + identifier + ".");
        }

        return parent.resolveScope(identifier);
    }

    public Boolean isFinal(String identifier) {
        return finals.contains(identifier);
    }
}
