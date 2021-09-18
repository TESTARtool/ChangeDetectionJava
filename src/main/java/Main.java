import com.google.gson.GsonBuilder;
import com.orientechnologies.orient.core.db.*;
import com.orientechnologies.orient.core.record.*;
import Models.*;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {

   //    var setting = new OrientDbSetting();

//        register(IOrientDbSetting.class, OrientDbSetting.class);
//        register(IOrientDbFactory.class, OrientDbFactory.class);
//        register(IApplicationBuilder.class, OrientDbApplicationBuilder.class);
//
//        var setting = get(IOrientDbSetting.class);
//
//        System.out.println(setting.getDatabaseName());
//        System.out.println("Yeah next try");
//        System.out.println("");
//
//        var builder = get(IApplicationBuilder.class);
//        builder.getApplication("help", 1);

//        var applicationName1 = "exp";
//        var applicationVersion1 = 1;
//
//        var applicationName2 = "exp";
//        var applicationVersion2 = 2;
//
//        try(var orientDb = new OrientDB(orientDbUrl, OrientDBConfig.defaultConfig())){
//            try(var db = orientDb.open(database, user, password)) {
//
//                var identifier = abstractStateModelIdentifier("exp", "1", db);
//
//                var application = new Application("exp", 1);
//
//                if (identifier.isPresent()){
//                    application.setModelIdentifier(identifier.get());
//
//                    System.out.println(identifier.get().getValue());
//
//                    var states = abstractState(db, identifier.get());
//
//                    for(var state : states){
//                        application.addState(state);
//                    }
//
//                    var gson = new GsonBuilder()
//                            .setPrettyPrinting()
//                            .create();
//
//                    var json = gson.toJson(application);
//
//                    System.out.println(json);
//
//                    System.out.println("--------");
//
//                    var attributes = abstractStateModelAbstractionAttributes(db, identifier.get());
//
//                }
//                else {
//                    System.out.println("Exp : 1 not found in database");
//                }
//            }
//        }
    }





    public int add(int a, int b){
        return a+b;
    }

    public static Optional<Set> abstractStateModelAbstractionAttributes(ODatabaseSession sessionDB, ModelIdentifier modelIdentifier) {
        var sql = "SELECT FROM AbstractStateModel where modelIdentifier = :modelIdentifier";

        var params = new HashMap<String, Object>();
        params.put("modelIdentifier", modelIdentifier.getValue());

        try(var resultSet = sessionDB.query(sql,params)){
            return resultSet.stream()
                    .filter(x -> x.isVertex() && x.getVertex().isPresent())
                    .map(x -> x.getVertex().get())
                    .map(x -> (Set)x.getProperty("abstractionAttributes"))
                    .findFirst();
        }
    }

    public static Optional<StateId> abstractStateFromAction(ODatabaseSession sessionDB, AbstractActionId abstractActionId){
        var sql = "SELECT FROM AbstractAction where actionId = :abstractActionId";

        var params = new HashMap<String, Object>();
        params.put("abstractActionId", abstractActionId.getValue());

        try (var resultSet = sessionDB.query(sql, params)){
            return resultSet.stream()
                    .filter(x -> x.isEdge() && x.getEdge().isPresent())
                    .map(x -> x.getEdge().get())
                    .map(x -> new StateId(x.getVertex(ODirection.IN).getProperty(StateId.PROPERTY_NAME)))
                    .findFirst();
        }
    }

    public static Set<ApplicationState> abstractState(ODatabaseSession sessionDB, ModelIdentifier modelIdentifier){
        var sql = "SELECT FROM AbstractState where modelIdentifier = :modelIdentifier";

        var params = new HashMap<String, Object>();
        params.put("modelIdentifier", modelIdentifier.getValue());

        try(var resultSet = sessionDB.query(sql, params)){

            return  resultSet.stream()
                    .filter(x -> x.isVertex() && x.getVertex().isPresent())
                    .map(x -> new StateId(x.getVertex().get().getProperty(StateId.PROPERTY_NAME)))
                    .map(x -> new ApplicationState(x))
                    .collect(Collectors.toSet())
                    ;
        }
    }

    public static Optional<ModelIdentifier> abstractStateModelIdentifier(String appName, String appVer, ODatabaseSession sessionDB) {

        var sql = "SELECT FROM AbstractStateModel where applicationName = :applicationName and "
                + "applicationVersion = :applicationVersion";

        var params = new HashMap<String, Object>();
        params.put("applicationName", appName);
        params.put("applicationVersion", appVer);

        try(var resultSet = sessionDB.query(sql, params)) {

            return resultSet.stream()
                    .filter(x -> x.isVertex() && x.getVertex().isPresent())
                    .filter(x -> x.getVertex().isPresent())
                    .map(x -> x.getVertex().get().getProperty(ModelIdentifier.PROPERTY_NAME))
                    .map(x -> new ModelIdentifier((String) x))
                    .findFirst();
        }
    }

}

