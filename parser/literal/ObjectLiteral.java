package parser.literal;
import java.util.List;

import parser.NodeType;
import parser.expr.Expr;
import runtime.Environment;
import runtime.values.ObjectValue;
import runtime.values.RuntimeValue;

public class ObjectLiteral extends Expr {
    private List<Property> properties;
    
    public ObjectLiteral(List<Property> properties) {
        super(NodeType.ObjectLiteral);
        this.properties = properties;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public String toString() {
        return "{ type: " + super.type + ", properties: " + properties.toString() + " }";
    }

    public RuntimeValue evaluate(Environment environment) {
        ObjectValue object = new ObjectValue();
        for (Property property : properties) {
            // if value is null then we're using { key } otherwise it's { key : value }
            // if it's { key } then it's a variable so we get it from the environment
            // otherwise we evaluate the value expression
            RuntimeValue value = property.getValue() == null ? environment.getVariableValue(property.getKey()) : property.getValue().evaluate(environment);

            object.addProperty(property.getKey(), value);
        }

        return object;
    }
}
