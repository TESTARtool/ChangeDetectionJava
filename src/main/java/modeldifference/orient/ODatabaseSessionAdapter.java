package modeldifference.orient;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.sql.OCommandSQLParsingException;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.util.Map;

public class ODatabaseSessionAdapter implements IODatabaseSession {

    private final ODatabaseSession databaseSession;

    public ODatabaseSessionAdapter(ODatabaseSession databaseSession){
        this.databaseSession = databaseSession;
    }

    public OResultSet query(String query, Map args) throws OCommandSQLParsingException, OCommandExecutionException {
        return databaseSession.query(query, args);
    }
}
