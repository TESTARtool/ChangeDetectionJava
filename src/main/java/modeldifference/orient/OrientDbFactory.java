package modeldifference.orient;

import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.db.*;
import settings.ISettingProvider;
import settings.ISettingsFor;

import java.util.Optional;

public class OrientDbFactory implements IOrientDbFactory {

    private final ISettingsFor<OrientDbSetting> settingsOrientDb;
    private Optional<ODatabaseSessionAdapter> databaseSessionCache;

    public OrientDbFactory(ISettingProvider settingProvider){
        this.settingsOrientDb = settingProvider.resolve(OrientDbSetting.class);
        this.databaseSessionCache = Optional.empty();
    }

    public IODatabaseSession openDatabase() {
        if (databaseSessionCache.isPresent()){
            return databaseSessionCache.get();
        }

        if (!settingsOrientDb.isValid()){
            throw new RuntimeException("Orient Db settings invalid");
        }

        var settings = settingsOrientDb.getValue();
        var orientDb = new OrientDB(settings.url, settings.getConfig());
        var session = orientDb.open(settings.databaseName, settings.userName, settings.password);
        var adapter = new ODatabaseSessionAdapter(session);

        databaseSessionCache = Optional.of(adapter);
        return adapter;
    }
}
