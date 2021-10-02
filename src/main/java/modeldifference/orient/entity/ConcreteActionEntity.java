package modeldifference.orient.entity;

import modeldifference.models.ConcreteActionId;

public class ConcreteActionEntity {

    private final ConcreteActionId id;
    private final String description;

    public ConcreteActionEntity(ConcreteActionId id, String description){
        this.id = id;
        this.description = description;
    }

    public ConcreteActionId getId() { return this.id;}
    public String getDescription() { return this.description;}
}
