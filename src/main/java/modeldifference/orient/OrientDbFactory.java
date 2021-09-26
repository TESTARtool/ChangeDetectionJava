package modeldifference.orient;

import com.orientechnologies.orient.core.db.*;

public class OrientDbFactory implements IOrientDbFactory {

    private final IOrientDbSetting settings;

    public OrientDbFactory(IOrientDbSetting settings){
        this.settings = settings;
    }

    public IODatabaseSession openDatabase() {
        try(var orientDb = new OrientDB(settings.getUrl(), settings.getConfig())) {
            try(var session = orientDb.open(settings.getDatabaseName(), settings.getUserName(), settings.getPassword()))
            {
                return new ODatabaseSessionAdapter(session);
            }
        }
    }
}
