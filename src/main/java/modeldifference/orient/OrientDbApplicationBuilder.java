package modeldifference.orient;

import com.orientechnologies.orient.core.record.OEdge;
import modeldifference.IApplicationBuilder;
import modeldifference.models.*;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.ODirection;

import java.util.*;
import java.util.stream.Collectors;

public class OrientDbApplicationBuilder implements IApplicationBuilder {

    private final IOrientDbFactory orientDbFactory;
    private final IAbstractStateModelEntityQuery abstractStateModelQuery;
    private final IAbstractStateEntityQuery abstractStateEntityQuery;

    public OrientDbApplicationBuilder(IOrientDbFactory orientDbFactory, IAbstractStateModelEntityQuery abstractStateModelQuery, IAbstractStateEntityQuery abstractStateEntityQuery){
        this.orientDbFactory = orientDbFactory;
        this.abstractStateModelQuery = abstractStateModelQuery;
        this.abstractStateEntityQuery = abstractStateEntityQuery;
    }

    public Optional<Application> getApplication(String applicationName, int version) {

        var orientDb = orientDbFactory.openDatabase();

        var entity = abstractStateModelQuery.query(applicationName, version, orientDb);

        if (entity.isEmpty()){
            return Optional.empty();
        }

        var application = new Application(applicationName, version)
                .setModelIdentifier(entity.get().getModelIdentifier());

        entity.get().getAbstractionAttributes()
                .forEach(application::addAbstractAttribute);

        abstractStateEntityQuery.query(application.getAbstractIdentifier(), orientDb)
                .stream()
                .map(x -> x.getStateIds())
                .flatMap(x -> x.stream())
                .map(x -> new AbstractStateId(x))
                .forEach(application::addAbstractStateId);

        return Optional.of(application);
    }


    public Map<AbstractActionId, String> outgoingActionIdDesc(IODatabaseSession sessionDB, ModelIdentifier modelIdentifier, AbstractActionId abstractStateId) {

        var sql = "SELECT FROM AbstractState where modelIdentifier = :modelIdentifier and stateId = :abstractStateId";

        var command = new OrientDbCommand(sql)
                .addParameter("modelIdentifier", modelIdentifier)
                .addParameter("abstractStateId", abstractStateId);

        try(var result = command.executeReader(sessionDB)){
            return result.stream()
                    .filter(x -> x.isVertex() && x.getVertex().isPresent())
                    .map(x -> x.getVertex().get())
                    .map(x -> x.getEdges(ODirection.OUT))
                    .map(x -> new AbstractActionId(((OEdge)x).getProperty("actionId")))
                    .collect(Collectors.toMap(x -> x, x-> concreteActionDescription(x, sessionDB)));
        }
    }

   public String concreteActionDescription(AbstractActionId abstractActionId, IODatabaseSession sessionDB) {

       var sql = "SELECT FROM AbstractAction WHERE actionId = :actionId";

       var command = new OrientDbCommand(sql)
               .addParameter("actionId", abstractActionId);

       try(var resultSet = command.executeReader(sessionDB)){
           var concreteActionIds = resultSet.stream()
                   .filter(x -> x.isEdge() && x.getEdge().isPresent())
                   .map(x -> x.getEdge().get())
                   .map(x -> (Set<String>)x.getProperty("concreteActionIds"))
                   .flatMap(Set::stream)
                   .collect(Collectors.toSet());

           return concreteActionIds.stream()
                   .map(x -> descriptionFromConcreteAction(x, sessionDB))
                   .filter(Optional::isPresent)
                   .map((Optional::get))
                   .findFirst()
                   .orElse("");
       }
   }

   private Optional<String> descriptionFromConcreteAction(String concreteActionId, IODatabaseSession sessionDB){

       var sql =  "SELECT FROM ConcreteAction WHERE actionId = :actionId";

       var command = new OrientDbCommand(sql)
               .addParameter("actionId", concreteActionId);

       try(var resultSet = command.executeReader(sessionDB)) {
           return resultSet.stream()
                   .filter(x -> x.isEdge() && x.getEdge().isPresent())
                   .map(x -> x.getEdge().get())
                   .map(x -> (String) x.getProperty("Desc"))
                   .findFirst();
       }
   }

    private Optional<StateId> abstractStateFromAction(IODatabaseSession sessionDB, AbstractActionId abstractActionId){
        var sql = "SELECT FROM AbstractAction where actionId = :abstractActionId";

        var command = new OrientDbCommand(sql)
                .addParameter("abstractActionId", abstractActionId);

        try (var resultSet = command.executeReader(sessionDB)){
            return resultSet.stream()
                    .filter(x -> x.isEdge() && x.getEdge().isPresent())
                    .map(x -> x.getEdge().get())
                    .map(x -> new StateId(x.getVertex(ODirection.IN).getProperty("stateId")))
                    .findFirst();
        }
    }
}
