package ifn701.safeguarder.backend.dao;

import java.sql.Connection;

import ifn701.safeguarder.backend.connection.ConnectionProvider;


public class DAOBase {
    private static Connection connection;

    public Connection getConnection() {
        if(connection == null) {
            ConnectionProvider conProvider = new ConnectionProvider();
            connection = conProvider.getConnection();
        }
        return connection;
    }
}
