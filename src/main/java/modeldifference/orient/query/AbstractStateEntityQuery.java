package modeldifference.orient.query;

import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import modeldifference.models.AbstractStateId;
import modeldifference.models.ModelIdentifier;
import modeldifference.orient.IAbstractStateEntityQuery;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.OrientDbCommand;
import modeldifference.orient.entity.AbstractState;

import java.util.*;
import java.util.stream.Collectors;

public class AbstractStateEntityQuery implements IAbstractStateEntityQuery {

    public List<AbstractState> query(ModelIdentifier modelIdentifier, IODatabaseSession sessionDB){
        var sql = "SELECT FROM AbstractState where modelIdentifier = :modelIdentifier";

        var command = new OrientDbCommand(sql)
                .addParameter("modelIdentifier", modelIdentifier);

        try(var resultSet = command.executeReader(sessionDB)){

            var xxx = resultSet.vertexStream()
                    .findFirst()
                    .get();

            var edges = xxx.getEdges(ODirection.OUT);

            for (OEdge ed: edges) {
                var edge = (OEdge)ed;
                var props = edge.getPropertyNames();
                for (var prop : props){
                    System.out.println(prop);
                }
            }


            return resultSet.vertexStream()
                    .map(this::mapToAbstractState)
                    .collect(Collectors.toList());
        }
    }

    private AbstractState mapToAbstractState(OVertex result){
        return new AbstractState(
                new AbstractStateId(result.getProperty("stateId")),
                new ModelIdentifier(result.getProperty("modelIdentifier")),
                (Set)result.getProperty("concreteStateIds"),
                (boolean)result.getProperty("isInitial"),
                (int)result.getProperty("counter")
        );
    }

}
