package modeldifference.orient.entity;

import modeldifference.models.ModelIdentifier;

import java.util.Set;

public class AbstractStateModel {
    private int applicationVersion;
    private ModelIdentifier modelIdentifier;
    private Set<String> abstractionAttributes;
    private String applicationName;

    public AbstractStateModel(int applicationVersion, String modelIdentifier, Set<String> abstractionAttributes, String applicationName){

        this.applicationVersion = applicationVersion;
        this.modelIdentifier = new ModelIdentifier(modelIdentifier);
        this.abstractionAttributes = abstractionAttributes;
        this.applicationName = applicationName;
    }

    public int getApplicationVersion(){
        return this.applicationVersion;
    }

    public ModelIdentifier getModelIdentifier(){
        return this.modelIdentifier;
    }

    public Set<String> getAbstractionAttributes(){
        return this.abstractionAttributes;
    }

    public String getApplicationName(){
        return this.applicationName;
    }
}
