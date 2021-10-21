package modeldifference.orient.query;

import com.orientechnologies.orient.core.record.OEdge;
import modeldifference.models.ConcreteActionId;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.IOrientDbFactory;
import modeldifference.orient.OrientDbCommand;
import modeldifference.orient.entity.ConcreteActionEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ConcreteActionEntityQuery  implements IConcreteActionEntityQuery {

    private final IOrientDbFactory orientDbFactory;

    public ConcreteActionEntityQuery(IOrientDbFactory orientDbFactory){
        this.orientDbFactory = orientDbFactory;
    }


    public List<ConcreteActionEntity> query(ConcreteActionId concreteActionId) {
        var sql =  "SELECT FROM ConcreteAction WHERE actionId = :actionId";

        var command = new OrientDbCommand(sql)
                .addParameter("actionId", concreteActionId);

        try(var resultSet = command.executeReader(orientDbFactory.openDatabase())) {
            return resultSet.edgeStream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }
    }

    private ConcreteActionEntity map(OEdge result){
        return new ConcreteActionEntity(
                new ConcreteActionId(result.getProperty("actionId")),
                result.getProperty("Desc")
        );
    }
}
