package ifn701.safeguarder.backend.connection;

import com.google.appengine.api.utils.SystemProperty;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionProvider {
    public static String GAE_DATABASE_CONFIG_FILE = "WEB-INF/database_properties/gae_connection.properties";
    public static String LOCAL_DATABASE_CONFIG_FILE = "WEB-INF/database_properties/local_connection.properties";

    Connection connection = null;

    ConnectionProperty conProperties = null;
    Logger log = Logger.getLogger(ConnectionProvider.class.getName());

    public Connection getConnection() {
        if (connection == null) {
            conProperties = getConnectionProperty();
            System.out.print(conProperties);
            try {
                if (isGoogleAppEngineServer()) {
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                    String url =
                            "jdbc:google:mysql://safeguarder-1097:safeguarder?user=root";

                    connection = DriverManager.getConnection(url);
                } else {
                    Class.forName(conProperties.getDriverClassName());
                    connection = DriverManager.getConnection(conProperties.getUrl(),
                            conProperties.getUsername(), conProperties.getPassword());
                }
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                log.warning(e.toString());
            }
        }
        return connection;
    }

    public ConnectionProperty getConnectionProperty() {
        if (isGoogleAppEngineServer() == true) {
           return getGAEConnectionProperty();
        } else {
           return getLocalConnectionProperty();
        }
    }

    public ConnectionProperty getLocalConnectionProperty() {
        return getPropertiesFromFile(LOCAL_DATABASE_CONFIG_FILE);
    }

    public ConnectionProperty getGAEConnectionProperty() {
         return getPropertiesFromFile(GAE_DATABASE_CONFIG_FILE);
    }

    public ConnectionProperty getPropertiesFromFile(String filename) {
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(filename);
            prop.load(fis);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return new ConnectionProperty(prop);
    }

    /**
     * Checking if the Server is running locally or on google app engine server
     * @return true if the server is running on app engine server
     */
    public boolean isGoogleAppEngineServer() {
        System.out.println(SystemProperty.environment.value());
        System.out.println(SystemProperty.Environment.Value.Production);
        return SystemProperty.environment.value() ==
                SystemProperty.Environment.Value.Production;
    }

    public ConnectionProperty getConProperties() {
        return conProperties;
    }

    public void setConProperties(ConnectionProperty conProperties) {
        this.conProperties = conProperties;
    }
}
