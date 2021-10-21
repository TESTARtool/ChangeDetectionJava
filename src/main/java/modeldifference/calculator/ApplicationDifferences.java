package modeldifference.calculator;

import modeldifference.models.AbstractState;
import modeldifference.models.Application;

import java.util.List;

public class ApplicationDifferences {

    private final Application firstVersion;
    private final Application secondVersion;
    private final List<AbstractState> removedState;
    private final List<AbstractState> addedState;

    public ApplicationDifferences(Application firstVersion, Application secondVersion, List<AbstractState> removedState, List<AbstractState> addedState) {
        this.firstVersion = firstVersion;
        this.secondVersion = secondVersion;

        this.removedState = removedState;
        this.addedState = addedState;
    }

    public List<AbstractState> getRemovedStates() {
        return removedState;
    }

    public List<AbstractState> getAddedStates() {
        return addedState;
    }
    public Application getFirstVersion() { return firstVersion;}
    public Application getSecondVersion() { return secondVersion;}


}
