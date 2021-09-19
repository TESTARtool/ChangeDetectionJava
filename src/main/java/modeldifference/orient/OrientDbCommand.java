package modeldifference.orient;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import modeldifference.models.IIdentifier;

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

    public OrientDbCommand addParameter(String name, IIdentifier identifier) {
        this.parameters.put(name, identifier.getValue());
        return this;
    }

    public OResultSet executeReader(ODatabaseSession sessionDB) {
        return sessionDB.query(this.query, this.parameters);
    }
}
