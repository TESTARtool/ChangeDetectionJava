package Models;

public class StateId{
    public static final String PROPERTY_NAME = "stateId";

    private String value;
    public StateId(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}