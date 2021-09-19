package modeldifference.orient;

import com.orientechnologies.orient.core.db.*;

public class OrientDbFactory implements IOrientDbFactory {

    public ODatabaseSession openDatabase(IOrientDbSetting setting) {
        var orientDb = new OrientDB(setting.getUrl(), setting.getConfig());
        return orientDb.open(setting.getDatabaseName(), setting.getUserName(), setting.getPassword());
    }

}
