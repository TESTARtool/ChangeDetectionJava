package modeldifference.models;

public class AbstractActionId implements IIdentifier{
    private String value;
    public AbstractActionId(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
