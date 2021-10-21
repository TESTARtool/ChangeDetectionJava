package modeldifference.orient.entity;

import modeldifference.models.AbstractActionId;
import modeldifference.models.AbstractStateId;
import modeldifference.models.ConcreteStateId;
import modeldifference.models.ModelIdentifier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AbstractStateEntity {
    private final AbstractStateId id;
    private final ModelIdentifier modelIdentifier;
    private final Set<ConcreteStateId> concreteStateIds;
    private final boolean isInitial;
    private final int counter;
    private final List<AbstractActionId> outgoingActionIds;
    private final List<AbstractActionId> incomingActionIds;

    public AbstractStateEntity(AbstractStateId id, ModelIdentifier modelIdentifier, Set<String> concreteStateIds, boolean isInitial, int counter, List<AbstractActionId> outgoingActionIds, List<AbstractActionId> incomingActionIds){
        this.id = id;
        this.modelIdentifier = modelIdentifier;
        this.isInitial = isInitial;
        this.counter = counter;
        this.outgoingActionIds = outgoingActionIds;
        this.incomingActionIds = incomingActionIds;
        this.concreteStateIds = concreteStateIds.stream()
                .map(x -> new ConcreteStateId(x))
                .collect(Collectors.toSet());
    }

    public Set<ConcreteStateId> getConcreteStateIdsStateIds(){
        return concreteStateIds;
    }

    public AbstractStateId getId(){
        return this.id;
    }

    public ModelIdentifier getModelIdentifier(){
        return this.modelIdentifier;
    }

    public boolean getIsInitial(){
        return this.isInitial;
    }

    public int getCounter(){
        return this.counter;
    }

    public List<AbstractActionId> getOutgoingActionIds() { return this.outgoingActionIds;}

    public List<AbstractActionId> getIncomingActionIds() { return this.incomingActionIds; }

}
