package modeldifference.orient.entity;

import modeldifference.models.AbstractStateId;
import modeldifference.models.ModelIdentifier;
import java.util.Set;

public class AbstractState {
    private final AbstractStateId id;
    private ModelIdentifier modelIdentifier;
    private Set concreteStateIds;
    private boolean isInitial;
    private int counter;

    public AbstractState(AbstractStateId id, ModelIdentifier modelIdentifier, Set<String> concreteStateIds, boolean isInitial, int counter){
        this.id = id;
        this.modelIdentifier = modelIdentifier;
        this.concreteStateIds = concreteStateIds;
        this.isInitial = isInitial;
        this.counter = counter;
    }

    public Set<String> getStateIds(){
        return concreteStateIds;
    }
}
