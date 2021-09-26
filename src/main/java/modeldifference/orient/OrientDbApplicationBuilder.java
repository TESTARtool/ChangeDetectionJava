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

        try (var orientDb = orientDbFactory.openDatabase()){

            var entity = abstractStateModelQuery.query(applicationName, version, orientDb);

            if (entity.isEmpty()){
                return Optional.empty();
            }

            var application = new Application(applicationName, version)
                    .setModelIdentifier(entity.get().getModelIdentifier());

            entity.get().getAbstractionAttributes()
                    .forEach(application::addAbstractAttribute);

            var states = abstractStateEntityQuery.query(application.getAbstractIdentifier(), orientDb);
            states.stream()
                .map(x -> x.getStateIds())
                .flatMap(x -> x.stream())
                .map(x -> new AbstractStateId(x))
                .forEach(application::addAbstractStateId);



            return Optional.of(application);
        }
        catch(Exception ex){
            System.out.println("error");
        }

        return Optional.empty();
    }

/*
    public Map<AbstractActionId, String> outgoingActionIdDesc(IODatabaseSession sessionDB, ModelIdentifier modelIdentifier, AbstractActionId abstractStateId) {

        var sql = "SELECT FROM AbstractState where modelIdentifier = :modelIdentifier and stateId = :abstractStateId";

        var command = new OrientDbCommand(sql)
                .addParameter("modelIdentifier", modelIdentifier)
                .addParameter("abstractStateId", abstractStateId);

        try(var result = command.executeReader(sessionDB)){
            var xxx = result.vertexStream()
                    .map(x -> x.getEdges(ODirection.OUT))
                    .map(x ->(OEdge)x)
                    .collect(Collectors.toList());

            var props = xxx.stream()
                    .findFirst()
                    .map(x -> x.getPropertyNames())
                    ;

            props.get()
                    .forEach(x -> System.out.println(x));

            return xxx.stream()
                    .map(x -> new AbstractActionId(((OEdge)x).getProperty("actionId")))
                    .collect(Collectors.toMap(x -> x, x-> concreteActionDescription(x, sessionDB)));
        }
    }

 */

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
                   .map(x -> new ConcreteActionId(x))
                   .collect(Collectors.toSet());

           return concreteActionIds.stream()
                   .map(x -> descriptionFromConcreteAction(x, sessionDB))
                   .filter(Optional::isPresent)
                   .map((Optional::get))
                   .findFirst()
                   .orElse("");
       }
   }

   private Optional<String> descriptionFromConcreteAction(ConcreteActionId concreteActionId, IODatabaseSession sessionDB){

       var sql =  "SELECT FROM ConcreteAction WHERE actionId = :actionId";

       var command = new OrientDbCommand(sql)
               .addParameter("actionId", concreteActionId);

       try(var resultSet = command.executeReader(sessionDB)) {
           return resultSet.edgeStream()
                   .map(x -> (String)x.getProperty("Desc"))
                   .findFirst();
       }
   }

    private Optional<StateId> abstractStateFromAction(IODatabaseSession sessionDB, AbstractActionId abstractActionId){
        var sql = "SELECT FROM AbstractAction where actionId = :abstractActionId";

        var command = new OrientDbCommand(sql)
                .addParameter("abstractActionId", abstractActionId);

        try (var resultSet = command.executeReader(sessionDB)){
            return resultSet.edgeStream()
                    .map(x -> new StateId(x.getVertex(ODirection.IN).getProperty("stateId")))
                    .findFirst();
        }
    }
}
