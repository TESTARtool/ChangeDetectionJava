package modeldifference.calculator;

import modeldifference.models.AbstractStateId;
import modeldifference.orient.entity.AbstractState;

import java.util.List;
import java.util.Set;

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
