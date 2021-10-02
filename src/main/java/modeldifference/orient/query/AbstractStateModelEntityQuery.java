package modeldifference.orient.query;

import com.orientechnologies.orient.core.record.OVertex;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.OrientDbCommand;
import modeldifference.orient.entity.AbstractStateModel;

import java.util.Optional;
import java.util.Set;

public class AbstractStateModelEntityQuery implements IAbstractStateModelEntityQuery {

    public Optional<AbstractStateModel> query(String applicationName, int version, IODatabaseSession sessionDb) {
        var sql = "SELECT FROM AbstractStateModel WHERE " +
                "applicationName = :applicationName AND " +
                "applicationVersion = :applicationVersion";

        var command = new OrientDbCommand(sql)
                .addParameter("applicationName", applicationName)
                .addParameter("applicationVersion", version);

        try (var resultSet = command.executeReader(sessionDb)) {

            return resultSet.vertexStream()
                    .map(this::mapToAbstractStateModel)
                    .findFirst();
        }
    }

    private AbstractStateModel mapToAbstractStateModel(OVertex result){
        return new AbstractStateModel(
                Integer.parseInt(result.getProperty("applicationVersion")),
                result.getProperty("modelIdentifier"),
                (Set)result.getProperty("abstractionAttributes"),
                result.getProperty("applicationName")
        );
    }
}
