package modeldifference.orient.entity;

import modeldifference.models.AbstractActionId;
import modeldifference.models.AbstractStateId;
import modeldifference.models.ConcreteActionId;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AbstractActionEntity {
    private final AbstractActionId id;
    private final AbstractStateId incomingStateId;
    private final AbstractStateId outgoingStateId;
    private final List<ConcreteActionId> concreteActionIds;

    public AbstractActionEntity(AbstractActionId id, AbstractStateId incomingStateId, AbstractStateId outgoingStateId, Set<String> concreteActionIds){
        this.id = id;
        this.incomingStateId = incomingStateId;
        this.outgoingStateId = outgoingStateId;
        this.concreteActionIds = concreteActionIds.stream()
                .map(x -> new ConcreteActionId(x))
                .collect(Collectors.toList());
    }

    public AbstractActionId getId(){
        return this.id;
    }

    public List<ConcreteActionId> getConcreteActionIds() { return this.concreteActionIds;}
}
