package ifn701.safegurader.backend.local_database_test_cases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import ifn701.safeguarder.backend.connection.ConnectionProperty;
import ifn701.safeguarder.backend.connection.ConnectionProvider;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class LocalConnectionTest extends LocalDatabaseTest{

    ConnectionProvider conProvider = null;
    ConnectionProvider spyProvider;

    @Before
    public void setUp() {
        conProvider = new ConnectionProvider();
        Properties mockProp = mock(Properties.class);
        when(mockProp.getProperty("properties.driverClassName")).thenReturn("com.mysql.jdbc.Driver");
        when(mockProp.getProperty("properties.url")).thenReturn("jdbc:mysql://localhost:3306/dementia");
        when(mockProp.getProperty("properties.username")).thenReturn("root");
        when(mockProp.getProperty("properties.password")).thenReturn("1231988");

        ConnectionProperty conProp = new ConnectionProperty(mockProp);

        spyProvider = spy(conProvider);
        doReturn(conProp).when(spyProvider).getConnectionProperty();
    }

    @After
    public void breakDown() {

    }

    @Test
    public void testLocalConnection(){
        assertNotNull(getSpiedConnectionProvider());
    }

    @Test
    public void testUserCRUD(){

    }
}