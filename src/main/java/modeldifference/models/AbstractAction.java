package modeldifference.models;

import java.util.Objects;

public class AbstractAction {
    private final AbstractActionId id;
    private final String description;

    public AbstractAction(AbstractActionId id, String description){

        this.id = id;
        this.description = description;
    }

    public AbstractActionId getId() { return this.id;}

    public String getDescription(){
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAction that = (AbstractAction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
