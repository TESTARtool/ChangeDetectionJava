import com.orientechnologies.orient.core.db.OrientDBConfig;

public class OrientDbSetting implements IOrientDbSetting {
    public String getUrl() {
        return "remote:192.168.188.60";
    }

    public String getDatabaseName() {
        return "testar";
    }

    public String getUserName() {
        return "testar";
    }

    public String getPassword() {
        return "testar";
    }

    public OrientDBConfig getConfig() {
        return OrientDBConfig.defaultConfig();
    }
}
