package modeldifference.orient;

import modeldifference.models.ModelIdentifier;
import modeldifference.orient.entity.AbstractState;

import java.util.List;

public interface IAbstractStateEntityQuery {
    List<AbstractState> query(ModelIdentifier modelIdentifier, IODatabaseSession sessionDB);
}
