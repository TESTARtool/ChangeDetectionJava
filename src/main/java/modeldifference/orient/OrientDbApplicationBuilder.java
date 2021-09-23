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
    private final IOrientDbSetting settings;

    public OrientDbApplicationBuilder(IOrientDbFactory orientDbFactory, IOrientDbSetting settings){

        this.orientDbFactory = orientDbFactory;
        this.settings = settings;
    }

    public Optional<Application> getApplication(String applicationName, int version) {

        try(var orientDb = orientDbFactory.openDatabase(settings)){
            var identifier = abstractStateModelIdentifier(applicationName, version, orientDb);

            if (identifier.isEmpty()){
                return Optional.empty();
            }

            var application = new Application(applicationName, version)
                    .setModelIdentifier(identifier.get());

            abstractState(orientDb, identifier.get())
                    .forEach(application::addAbstractStateId);

            abstractStateModelAbstractionAttributes(orientDb, identifier.get())
                    .forEach(application::addAttribute);

            return Optional.of(application);
        }
    }


    public Map<AbstractActionId, String> outgoingActionIdDesc(ODatabaseSession sessionDB, ModelIdentifier modelIdentifier, AbstractActionId abstractStateId) {

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

   public String concreteActionDescription(AbstractActionId abstractActionId, ODatabaseSession sessionDB) {

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

   private Optional<String> descriptionFromConcreteAction(String concreteActionId, ODatabaseSession sessionDB){

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

    private Optional<ModelIdentifier> abstractStateModelIdentifier(String applicationName, int version, ODatabaseSession sessionDB) {

        var sql = "SELECT FROM AbstractStateModel where applicationName = :applicationName and "
                + "applicationVersion = :applicationVersion";

        var command = new OrientDbCommand(sql)
            .addParameter("applicationName", applicationName)
            .addParameter("applicationVersion", version);

        try(var resultSet = command.executeReader(sessionDB)) {

            return resultSet.stream()
                    .filter(x -> x.isVertex() && x.getVertex().isPresent())
                    .filter(x -> x.getVertex().isPresent())
                    .map(x -> x.getVertex().get().getProperty("modelIdentifier"))
                    .map(x -> new ModelIdentifier((String) x))
                    .findFirst();
        }
    }

    private Set<String> abstractStateModelAbstractionAttributes(ODatabaseSession sessionDB, ModelIdentifier modelIdentifier) {
        var sql = "SELECT FROM AbstractStateModel where modelIdentifier = :modelIdentifier";

        var command = new OrientDbCommand(sql)
            .addParameter("modelIdentifier", modelIdentifier);

        try(var resultSet = command.executeReader(sessionDB)){
            var result = resultSet.stream()
                    .filter(x -> x.isVertex() && x.getVertex().isPresent())
                    .map(x -> x.getVertex().get())
                    .map(x -> (Set<String>)x.getProperty("abstractionAttributes"))
                    .findFirst();

            return result.orElseGet(HashSet::new);
        }
    }

    private Optional<StateId> abstractStateFromAction(ODatabaseSession sessionDB, AbstractActionId abstractActionId){
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

    private Set<AbstractStateId> abstractState(ODatabaseSession sessionDB, ModelIdentifier modelIdentifier){
        var sql = "SELECT FROM AbstractState where modelIdentifier = :modelIdentifier";

        var command = new OrientDbCommand(sql)
                .addParameter("modelIdentifier", modelIdentifier);

        try(var resultSet = command.executeReader(sessionDB)){
            return  resultSet.stream()
                    .filter(x -> x.isVertex() && x.getVertex().isPresent())
                    .map(x -> new AbstractStateId(x.getVertex().get().getProperty("stateId")))
                    .collect(Collectors.toSet())
                    ;
        }
    }
}
