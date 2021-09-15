package Models;

import java.util.ArrayList;
import java.util.List;

public class ModelIdentifier {
    public static final String PROPERTY_NAME = "modelIdentifier";
    private String value;

    public ModelIdentifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

