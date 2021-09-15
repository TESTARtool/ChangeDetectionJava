package Models;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private String name;
    private int version;
    private ModelIdentifier abstractIdentifier;
    private List<ApplicationState> states;

    public Application(String name, int version){
        this.name = name;
        this.version = version;
        this.states = new ArrayList<>();
    }

    public String getName(){
        return name;
    }
    public int getVersion(){
        return version;
    }
    public void setModelIdentifier(ModelIdentifier identifier){
        this.abstractIdentifier = identifier;
    }

    public ModelIdentifier getAbstractIdentifier(){
        return this.abstractIdentifier;
    }

    public void addState(ApplicationState state){
        states.add(state);
    }

    private List<ApplicationState> getStates() {
        return states;
    }
}