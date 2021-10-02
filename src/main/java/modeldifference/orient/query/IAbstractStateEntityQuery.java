package modeldifference.orient.query;

import modeldifference.models.AbstractStateId;
import modeldifference.models.ModelIdentifier;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.entity.AbstractState;

import java.util.List;
import java.util.Optional;

public interface IAbstractStateEntityQuery {
    Optional<AbstractState> query(ModelIdentifier modelIdentifier, AbstractStateId abstractStateId, IODatabaseSession sessionDB);
    List<AbstractState> query(ModelIdentifier modelIdentifier, IODatabaseSession sessionDB);
}