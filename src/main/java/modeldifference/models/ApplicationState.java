package modeldifference.models;

public class ApplicationState {
    private StateId id;
    public ApplicationState(StateId id){
        this.id = id;
    }

    public StateId getId(){
        return id;
    }
}