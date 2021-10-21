package modeldifference.orient.query;

import modeldifference.models.ConcreteStateId;
import modeldifference.orient.IODatabaseSession;

import java.nio.file.Path;

public interface IWidgetTreeQuery {
    String query(Path location, ConcreteStateId concreteStateId);
}
