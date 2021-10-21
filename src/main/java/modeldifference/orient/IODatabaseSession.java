package modeldifference.orient;

import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.sql.OCommandSQLParsingException;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.io.Closeable;
import java.util.Map;

public interface IODatabaseSession {
    OResultSet query(String query, Map args) throws OCommandSQLParsingException, OCommandExecutionException;
}
