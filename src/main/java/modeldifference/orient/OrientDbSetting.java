package modeldifference.orient;

import com.orientechnologies.orient.core.db.OrientDBConfig;
import settings.IsSetting;

public class OrientDbSetting {

    @IsSetting(name = "OrientDb:url")
    public String url;

    @IsSetting(name = "OrientDb:databaseName")
    public String databaseName;

    @IsSetting(name = "OrientDb:userName")
    public String userName;

    @IsSetting(name = "OrientDb:password")
    public String password;

    public OrientDBConfig getConfig() {
        return OrientDBConfig.defaultConfig();
    }
}
