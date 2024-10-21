package parser;
import java.util.List;

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
        return "{ kind: " + super.kind + ", properties: " + properties.toString() + " }";
    }
}
