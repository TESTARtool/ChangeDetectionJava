package modeldifference.models;

public class AbstractAction {
    private final AbstractActionId id;
    private final String description;

    public AbstractAction(AbstractActionId id, String description){

        this.id = id;
        this.description = description;
    }
}
