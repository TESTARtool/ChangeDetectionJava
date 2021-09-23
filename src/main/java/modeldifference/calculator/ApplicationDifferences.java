package modeldifference.calculator;

import modeldifference.models.AbstractStateId;

import java.util.Set;

public class ApplicationDifferences {

    private final Set<AbstractStateId> removedState;
    private final Set<AbstractStateId> addedState;

    public ApplicationDifferences(Set<AbstractStateId> removedState, Set<AbstractStateId> addedState) {

        this.removedState = removedState;
        this.addedState = addedState;
    }

    public Set<AbstractStateId> getRemovedStates() {
        return removedState;
    }

    public Set<AbstractStateId> getAddedStates() {
        return addedState;
    }
}
