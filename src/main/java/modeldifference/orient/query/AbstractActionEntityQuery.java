package modeldifference.orient.query;

import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import modeldifference.models.AbstractActionId;
import modeldifference.models.AbstractStateId;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.OrientDbCommand;
import modeldifference.orient.entity.AbstractActionEntity;

import java.util.Optional;
import java.util.Set;

public class AbstractActionEntityQuery  implements IAbstractActionEntityQuery{
    public Optional<AbstractActionEntity> query(AbstractActionId id, IODatabaseSession sessionDb){
        var sql = "SELECT FROM AbstractAction where actionId = :abstractActionId";
        var command = new OrientDbCommand(sql)
                .addParameter("abstractActionId", id);

        try(var resultSet = command.executeReader(sessionDb)){
            return resultSet.edgeStream()
                    .map(this::map)
                    .findFirst();
        }
    }

    private AbstractActionEntity map(OEdge result){
        return new AbstractActionEntity(
                new AbstractActionId(result.getProperty("actionId")),
                new AbstractStateId(result.getVertex(ODirection.IN).getProperty("stateId")),
                new AbstractStateId(result.getVertex(ODirection.OUT).getProperty("stateId")),
                (Set<String>)result.getProperty("concreteActionIds")
        );
    }
}
