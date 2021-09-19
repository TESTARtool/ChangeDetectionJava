package modeldifference.models;

public class ModelIdentifier implements IIdentifier {
    public static final String PROPERTY_NAME = "modelIdentifier";
    private String value;

    public ModelIdentifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

