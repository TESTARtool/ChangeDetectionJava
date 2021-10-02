package modeldifference.orient.entity;

import com.orientechnologies.orient.core.record.impl.ORecordBytes;
import modeldifference.models.ConcreteStateId;

public class ConcreteStateEntity {

    private final ConcreteStateId id;
    private final ORecordBytes screenshotBytes;

    public ConcreteStateEntity(ConcreteStateId id, ORecordBytes screenshotBytes){
        this.id = id;
        this.screenshotBytes = screenshotBytes;
    }
}
