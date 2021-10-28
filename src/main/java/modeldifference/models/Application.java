package modeldifference.models;

import java.util.*;

public class Application {
    private String name;
    private String version;
    private ModelIdentifier abstractIdentifier;
    private Set<AbstractStateId> states;
    private SortedSet<String> attributes;

    public Application(String name, String version){
        this.name = name;
        this.version = version;
        this.states = new HashSet<>();
        this.attributes = new TreeSet<>();
    }

    public String getName(){
        return name;
    }
    public String getVersion(){
        return version;
    }

    public Application setModelIdentifier(ModelIdentifier identifier){
        this.abstractIdentifier = identifier;
        return this;
    }

    public ModelIdentifier getAbstractIdentifier(){
        return this.abstractIdentifier;
    }

    public void addAbstractStateId(AbstractStateId state){
        states.add(state);
    }

    public void addAbstractAttribute(String attribute) { attributes.add(attribute);}

    public Set<AbstractStateId> getAbstractStateIds() {
        return states;
    }

    public SortedSet<String> getAttributes(){
        return attributes;
    }
}