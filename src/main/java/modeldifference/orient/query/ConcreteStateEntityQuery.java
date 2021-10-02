package modeldifference.orient.query;

import com.orientechnologies.orient.core.record.OVertex;
import modeldifference.models.ConcreteStateId;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.OrientDbCommand;
import modeldifference.orient.entity.ConcreteStateEntity;

import java.util.Optional;
import java.util.stream.Collectors;

public class ConcreteStateEntityQuery implements IConcreteStateEntityQuery {

    public Optional<ConcreteStateEntity> query(ConcreteStateId stateId, IODatabaseSession sessionDb){
        var sql = "SELECT FROM ConcreteState WHERE ConcreteIDCustom = :concreteId LIMIT 1";

        var command = new OrientDbCommand(sql)
                .addParameter("concreteId", stateId);

        try(var resultSet = command.executeReader(sessionDb)){
            return resultSet.vertexStream()
                    .map(this::map)
                    .findFirst();
        }
    }

    private ConcreteStateEntity map(OVertex result){
        return  new ConcreteStateEntity(
                new ConcreteStateId(result.getProperty("ConcreteIDCustom")),
                result.getProperty("screenshot")
        );
    }
}
