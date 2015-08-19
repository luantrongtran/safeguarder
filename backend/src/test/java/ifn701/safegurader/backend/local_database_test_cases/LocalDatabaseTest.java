package ifn701.safegurader.backend.local_database_test_cases;

import java.sql.Connection;
import java.util.Properties;

import ifn701.safeguarder.backend.connection.ConnectionProperty;
import ifn701.safeguarder.backend.connection.ConnectionProvider;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class LocalDatabaseTest {

    private static Connection connection = null;

    public Connection getSpiedConnectionProvider() {
        if (connection == null) {
            ConnectionProvider conProvider = new ConnectionProvider();
            Properties mockProp = mock(Properties.class);
            when(mockProp.getProperty("properties.driverClassName")).thenReturn("com.mysql.jdbc.Driver");
            when(mockProp.getProperty("properties.url")).thenReturn("jdbc:mysql://localhost:3306/safeguarder");
            when(mockProp.getProperty("properties.username")).thenReturn("root");
            when(mockProp.getProperty("properties.password")).thenReturn("123456");

            ConnectionProperty conProp = new ConnectionProperty(mockProp);

            ConnectionProvider spyConnectionProvider = spy(conProvider);
            doReturn(conProp).when(spyConnectionProvider).getConnectionProperty();

            connection = spyConnectionProvider.getConnection();
            assertNotNull("Connection should not be null", connection);
        }
        return connection;
    }
}
