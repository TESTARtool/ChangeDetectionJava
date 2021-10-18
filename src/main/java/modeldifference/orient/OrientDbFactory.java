package modeldifference.orient;

import com.orientechnologies.orient.core.db.*;

import java.util.Optional;

public class OrientDbFactory implements IOrientDbFactory {

    private final IOrientDbSetting settings;
    private Optional<ODatabaseSessionAdapter> databaseSessionCache;

    public OrientDbFactory(IOrientDbSetting settings){
        this.settings = settings;
        this.databaseSessionCache = Optional.empty();
    }

    public IODatabaseSession openDatabase() {
        if (databaseSessionCache.isPresent()){
            return databaseSessionCache.get();
        }

        var orientDb = new OrientDB(settings.getUrl(), settings.getConfig());
        var session = orientDb.open(settings.getDatabaseName(), settings.getUserName(), settings.getPassword());
        var adapter = new ODatabaseSessionAdapter(session);

        databaseSessionCache = Optional.of(adapter);
        return adapter;
    }
}
