package modeldifference.orient.query;

import com.orientechnologies.orient.core.record.OEdge;
import modeldifference.models.ConcreteActionId;
import modeldifference.orient.IConcreteActionEntityQuery;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.OrientDbCommand;
import modeldifference.orient.entity.ConcreteAction;

import java.util.List;
import java.util.stream.Collectors;

public class ConcreteActionEntityQuery  implements IConcreteActionEntityQuery {
    public List<ConcreteAction> query(ConcreteActionId concreteActionId, IODatabaseSession sessionDB) {
        var sql =  "SELECT FROM ConcreteAction WHERE actionId = :actionId";

        var command = new OrientDbCommand(sql)
                .addParameter("actionId", concreteActionId);

        try(var resultSet = command.executeReader(sessionDB)) {
            return resultSet.edgeStream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }
    }

    private ConcreteAction map(OEdge result){
        return new ConcreteAction(
                new ConcreteActionId(result.getProperty("actionId")),
                result.getProperty("Desc")
        );
    }
}
