package modeldifference.orient;

import com.orientechnologies.orient.core.db.OrientDBConfig;
import application.settings.IsSetting;

public class OrientDbSetting {

    @IsSetting(name = "OrientDb:url", description = "Url of the OrientDb location, start with 'remote' to connect to a remote location")
    public String url;

    @IsSetting(name = "OrientDb:databaseName", description = "Name of the TESTAR database")
    public String databaseName;

    @IsSetting(name = "OrientDb:userName", description = "Username of the TESTAR database")
    public String userName;

    @IsSetting(name = "OrientDb:password", description = "Password of the TESTAR database (not the root).")
    public String password;

    public OrientDBConfig getConfig() {
        return OrientDBConfig.defaultConfig();
    }
}
