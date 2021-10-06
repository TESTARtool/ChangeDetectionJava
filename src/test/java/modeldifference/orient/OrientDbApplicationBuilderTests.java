package modeldifference.orient;

import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.sql.OCommandSQLParsingException;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import modeldifference.models.ModelIdentifier;
import modeldifference.orient.entity.AbstractStateEntity;
import modeldifference.orient.entity.AbstractStateModel;
import modeldifference.orient.query.IAbstractStateEntityQuery;
import modeldifference.orient.query.IAbstractStateModelEntityQuery;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrientDbApplicationBuilderTests {

    private final TestAdapter testAdapter;
    private final OrientDbApplicationBuilder sut;
    private final IAbstractStateEntityQuery abstractStateEntityQueryMock;
    private final IAbstractStateModelEntityQuery abstractStateModelQueryMock;

    public OrientDbApplicationBuilderTests(){
        abstractStateModelQueryMock = mock(IAbstractStateModelEntityQuery.class);
        abstractStateEntityQueryMock = mock(IAbstractStateEntityQuery.class);

        var orientDbFactoryMock = mock(IOrientDbFactory.class);
        testAdapter = new TestAdapter();
        when(orientDbFactoryMock.openDatabase()).thenReturn(testAdapter);

        sut = new OrientDbApplicationBuilder(orientDbFactoryMock, abstractStateModelQueryMock, abstractStateEntityQueryMock);
    }

    @Test
    void whenApplicationCannotBeFoundEmptyOptionalObjectIsReturned(){

        when(abstractStateModelQueryMock.query("Not Found", 404, testAdapter)).thenReturn(Optional.empty());

        var application = sut.getApplication("Not Found", 404);

        assertFalse(application.isPresent());
    }

    @Test
    void modelIdentifierAndAttributesAreLoadedFromTheDatabase(){
        var attributes = new HashSet<String>();
        attributes.add("att1");
        attributes.add("att2");

        when(abstractStateModelQueryMock.query("TILT", 1, testAdapter)).thenReturn(Optional.of(new AbstractStateModel(1, "abc", attributes, "TILT")));
        when(abstractStateEntityQueryMock.query(new ModelIdentifier("abc"), testAdapter)).thenReturn(new ArrayList<AbstractStateEntity>());

        var applicationOptional = sut.getApplication("TILT", 1);

        assertTrue(applicationOptional.isPresent());
        var application = applicationOptional.get();

        assertEquals("TILT", application.getName());
        assertEquals(1, application.getVersion());
        assertEquals("abc", application.getAbstractIdentifier().getValue());
    }

    class TestAdapter implements IODatabaseSession {

        public OResultSet query(String query, Map args) throws OCommandSQLParsingException, OCommandExecutionException {
            return null;
        }

        @Override
        public void close() throws IOException {

        }
    }
}
