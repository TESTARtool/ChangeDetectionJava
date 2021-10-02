package modeldifference.orient.query;

import modeldifference.models.ConcreteActionId;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.entity.ConcreteActionEntity;

import java.util.List;

public interface IConcreteActionEntityQuery {
    List<ConcreteActionEntity> query(ConcreteActionId concreteActionId, IODatabaseSession sessionDB);
}
