package modeldifference.orient;

import modeldifference.models.ConcreteActionId;
import modeldifference.orient.entity.ConcreteAction;

import java.util.List;

public interface IConcreteActionEntityQuery {
    List<ConcreteAction> query(ConcreteActionId concreteActionId, IODatabaseSession sessionDB);
}
