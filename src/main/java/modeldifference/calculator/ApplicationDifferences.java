package modeldifference.calculator;

import modeldifference.models.AbstractState;

import java.util.List;

public class ApplicationDifferences {

    private final List<AbstractState> removedState;
    private final List<AbstractState> addedState;

    public ApplicationDifferences(List<AbstractState> removedState, List<AbstractState> addedState) {

        this.removedState = removedState;
        this.addedState = addedState;
    }

    public List<AbstractState> getRemovedStates() {
        return removedState;
    }

    public List<AbstractState> getAddedStates() {
        return addedState;
    }
}
