package modeldifference.orient.query;

import modeldifference.models.AbstractStateId;
import modeldifference.models.ModelIdentifier;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.entity.AbstractStateEntity;

import java.util.List;
import java.util.Optional;

public interface IAbstractStateEntityQuery {
    Optional<AbstractStateEntity> query(ModelIdentifier modelIdentifier, AbstractStateId abstractStateId, IODatabaseSession sessionDB);
    List<AbstractStateEntity> query(ModelIdentifier modelIdentifier, IODatabaseSession sessionDB);
}
