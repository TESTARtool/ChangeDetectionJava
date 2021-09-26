package modeldifference.orient.entity;

import modeldifference.models.ConcreteActionId;

public class ConcreteAction {

    private final ConcreteActionId id;
    private final String description;

    public ConcreteAction(ConcreteActionId id, String description){
        this.id = id;

        this.description = description;
    }
}
