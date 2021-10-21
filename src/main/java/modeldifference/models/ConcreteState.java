package modeldifference.models;

import java.util.Objects;

public class ConcreteState {
    private final ConcreteStateId id;
    private final byte[] screenshot;

    public ConcreteState(ConcreteStateId id, byte[] screenshot){
        this.id = id;
        this.screenshot = screenshot;
    }

    public byte[] getScreenshot(){
        return this.screenshot;
    }

    public ConcreteStateId getId() {return this.id;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcreteState that = (ConcreteState) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
