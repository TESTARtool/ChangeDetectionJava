package modeldifference.orient.query;

import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.entity.AbstractStateModel;

import java.util.Optional;

public interface IAbstractStateModelEntityQuery {
    Optional<AbstractStateModel> query(String applicationName, int version, IODatabaseSession sessionDb);
}
