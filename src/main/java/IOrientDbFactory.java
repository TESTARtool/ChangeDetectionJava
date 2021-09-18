import com.orientechnologies.orient.core.db.ODatabaseSession;

public interface IOrientDbFactory {
    public ODatabaseSession OpenDatabase(String connectionString, String database, String userName, String password);
}
