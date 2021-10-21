package modeldifference.orient.query;

import modeldifference.models.AbstractActionId;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.entity.AbstractActionEntity;

import java.util.Optional;

public interface IAbstractActionEntityQuery {
    Optional<AbstractActionEntity> query(AbstractActionId id);
}
