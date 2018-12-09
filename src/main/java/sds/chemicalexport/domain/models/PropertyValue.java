package sds.chemicalexport.domain.models;

enum PropertyType {
    String,
    Int,
    Bool,
    Email,
    Url,
    Json,
    Double,
    Select,
    Date,
    Time,
    Datetime,
    Textarea
}

public class PropertyValue {

    private String Name;
    private Object Value;
    private PropertyType Type;

    public PropertyValue(String Name, Object Value, PropertyType Type) {
        this.Name = Name;
        this.Value = Value;
        this.Type = Type;
    }

    public PropertyValue(String Name, Object Value) {
        this.Name = Name;
        this.Value = Value;
    }

    public String getName() {
        return Name;
    }

    public Object getValue() {
        return Value;
    }

    public PropertyType getType() {
        return Type;
    }
}
