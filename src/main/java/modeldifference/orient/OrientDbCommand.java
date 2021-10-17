package modeldifference.orient;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import modeldifference.models.Identifier;

import java.util.HashMap;

public class OrientDbCommand {
    private final String query;
    private final HashMap<String, Object> parameters;

    public OrientDbCommand(String query) {
        this.query = query;
        this.parameters = new HashMap<>();
    }

    public OrientDbCommand addParameter(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

    public OrientDbCommand addParameter(String name, Identifier identifier) {
        this.parameters.put(name, identifier.getValue());
        return this;
    }

    public OResultSet executeReader(IODatabaseSession sessionDB) {
        return sessionDB.query(this.query, this.parameters);
    }

    public OResultSet executeReader(IOrientDbFactory orientDbFactory){
        return executeReader(orientDbFactory.openDatabase());
    }
}
