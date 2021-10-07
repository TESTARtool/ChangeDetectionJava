package modeldifference.orient.query;

import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import modeldifference.models.AbstractActionId;
import modeldifference.models.AbstractStateId;
import modeldifference.models.ModelIdentifier;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.OrientDbCommand;
import modeldifference.orient.entity.AbstractStateEntity;

import java.util.*;
import java.util.stream.Collectors;

public class AbstractStateEntityQuery implements IAbstractStateEntityQuery {

    public List<AbstractStateEntity> query(ModelIdentifier modelIdentifier, IODatabaseSession sessionDB){
        var sql = "SELECT FROM AbstractState WHERE modelIdentifier = :modelIdentifier";

        var command = new OrientDbCommand(sql)
                .addParameter("modelIdentifier", modelIdentifier);

        try(var resultSet = command.executeReader(sessionDB)){
            return resultSet.vertexStream()
                    .map(this::mapToAbstractState)
                    .collect(Collectors.toList());
        }
    }

    public Optional<AbstractStateEntity> query(ModelIdentifier modelIdentifier, AbstractStateId abstractStateId, IODatabaseSession sessionDB){
        var sql = "SELECT FROM AbstractState WHERE modelIdentifier = :modelIdentifier AND stateId = :abstractStateId";

        var command = new OrientDbCommand(sql)
                .addParameter("modelIdentifier", modelIdentifier)
                .addParameter("abstractStateId", abstractStateId);

        try(var resultSet = command.executeReader(sessionDB)){
            return resultSet.vertexStream()
                    .map(this::mapToAbstractState)
                    .findFirst();
        }
    }

    private AbstractStateEntity mapToAbstractState(OVertex result){
        return new AbstractStateEntity(
                new AbstractStateId(result.getProperty("stateId")),
                new ModelIdentifier(result.getProperty("modelIdentifier")),
                (Set<String>)result.getProperty("concreteStateIds"),
                (boolean)result.getProperty("isInitial"),
                (int)result.getProperty("counter"),
                outgoingAbstractActionIds(result),
                incomingAbstractActionIds(result)
        );
    }

    private List<AbstractActionId> outgoingAbstractActionIds(OVertex result){
        var actionIds = new ArrayList<AbstractActionId>();
        var edges = result.getEdges(ODirection.OUT);
        for (OEdge edge: edges) {
            actionIds.add(new AbstractActionId(edge.getProperty("actionId")));
        }

        return actionIds;
    }

    private List<AbstractActionId> incomingAbstractActionIds(OVertex result){
        var actionIds = new ArrayList<AbstractActionId>();
        var edges = result.getEdges(ODirection.IN);
        for (OEdge edge: edges) {
            actionIds.add(new AbstractActionId(edge.getProperty("actionId")));
        }

        return actionIds;
    }
}
