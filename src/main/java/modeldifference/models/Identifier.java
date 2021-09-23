package modeldifference.models;

import java.util.Objects;

public abstract class Identifier {

    private final String value;

    protected Identifier(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(value, that.value);
    }

    public int hashCode() {
        return Objects.hash(value);
    }
}
