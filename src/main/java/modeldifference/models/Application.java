package modeldifference.models;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private String name;
    private int version;
    private ModelIdentifier abstractIdentifier;
    private List<ApplicationState> states;
    private List<String> attributes;

    public Application(String name, int version){
        this.name = name;
        this.version = version;
        this.states = new ArrayList<>();
        this.attributes = new ArrayList<>();
    }

    public String getName(){
        return name;
    }
    public int getVersion(){
        return version;
    }

    public Application setModelIdentifier(ModelIdentifier identifier){
        this.abstractIdentifier = identifier;
        return this;
    }

    public ModelIdentifier getAbstractIdentifier(){
        return this.abstractIdentifier;
    }



    public void addState(ApplicationState state){
        states.add(state);
    }

    public void addAttribute(String attribute) { attributes.add(attribute);}

    private List<ApplicationState> getStates() {
        return states;
    }
}