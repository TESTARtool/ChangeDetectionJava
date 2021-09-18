import Models.Application;
import Models.ModelIdentifier;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import java.util.HashMap;
import java.util.Optional;

public class OrientDbApplicationBuilder implements IApplicationBuilder{

    private final IOrientDbFactory orientDbFactory;
    private final IOrientDbSetting settings;

    public OrientDbApplicationBuilder(IOrientDbFactory orientDbFactory, IOrientDbSetting settings){

        this.orientDbFactory = orientDbFactory;
        this.settings = settings;
    }

    public Optional<Application> getApplication(String applicationName, int version) {

        System.out.println("This is coming from builder: " + settings.getDatabaseName());

  //      var modelIdentifier = abstractStateModelIdentifier(applicationName, version);
        return Optional.empty();
    }

    private Optional<ModelIdentifier> abstractStateModelIdentifier(String appName, String appVer, ODatabaseSession sessionDB) {

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
