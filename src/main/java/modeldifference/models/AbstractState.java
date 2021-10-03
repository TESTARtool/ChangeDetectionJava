package modeldifference.models;

import java.util.List;

public class AbstractState {
    private final AbstractStateId id;
    private final byte[] screenshot;
    private final List<AbstractAction> actions;

    public AbstractState(AbstractStateId id, byte[] screenshot, List<AbstractAction> actions){
        this.id = id;
        this.screenshot = screenshot;
        this.actions = actions;
    }
}
