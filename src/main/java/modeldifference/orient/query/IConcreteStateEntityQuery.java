package modeldifference.orient.query;

import modeldifference.models.ConcreteStateId;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.entity.ConcreteStateEntity;

import java.util.Optional;

public interface IConcreteStateEntityQuery  {
    Optional<ConcreteStateEntity> query(ConcreteStateId stateId, IODatabaseSession sessionDb);
}
