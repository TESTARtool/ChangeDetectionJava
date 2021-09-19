package modeldifference.orient;

import com.orientechnologies.orient.core.db.OrientDBConfig;

public interface IOrientDbSetting {
    String getUrl();
    String getDatabaseName();
    String getUserName();
    String getPassword();
    OrientDBConfig getConfig();
}
