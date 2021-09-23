package modeldifference.models;

import java.util.*;

public class Application {
    private String name;
    private int version;
    private ModelIdentifier abstractIdentifier;
    private Set<AbstractStateId> states;
    private SortedSet<String> attributes;

    public Application(String name, int version){
        this.name = name;
        this.version = version;
        this.states = new HashSet<>();
        this.attributes = new TreeSet<>();
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

    public void addAbstractStateId(AbstractStateId state){
        states.add(state);
    }

    public void addAttribute(String attribute) { attributes.add(attribute);}

    public Set<AbstractStateId> getAbstractStateIds() {
        return states;
    }

    public SortedSet<String> getAttributes(){
        return attributes;
    }
}