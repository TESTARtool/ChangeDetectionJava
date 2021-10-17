package modeldifference.models;

import java.util.Objects;
import java.util.Set;

public class AbstractState {
    private final AbstractStateId id;
    private final Set<ConcreteState> concreteStates;
    private final Set<AbstractAction> outgoingActions;
    private final Set<AbstractAction> incomingActions;

    public AbstractState(AbstractStateId id, Set<ConcreteState> concreteStates, Set<AbstractAction> outgoingActions, Set<AbstractAction> incomingActions){
        this.id = id;
        this.concreteStates = concreteStates;
        this.outgoingActions = outgoingActions;
        this.incomingActions = incomingActions;
    }

    public byte[] getScreenshot(){
        return concreteStates.stream()
                .map(x -> x.getScreenshot())
                .findFirst()
                .orElse(new byte[0]);
    }

    public AbstractStateId getId() {
        return id;
    }

    public Set<AbstractAction> getOutgoingActions(){
        return outgoingActions;
    }

    public Set<AbstractAction> getIncomingActions() { return incomingActions; }

    public Set<ConcreteState> getConcreteStates(){ return this.concreteStates;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractState that = (AbstractState) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
