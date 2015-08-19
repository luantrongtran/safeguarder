package ifn701.safeguarder.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import ifn701.safeguarder.backend.entities.Accident;
import ifn701.safeguarder.backend.entities.Location;

public class AccidentDao extends DAOBase {
    public static String tableName = "accident";


    /**
     * Get all accidents in the area which has centroid is location variable
     * and radius which is radius variable.
     * @param location the centroid of the area
     * @param radius the radius within which the accidents are
     */
    public List<Accident> getAccidentsInSelectedArea(Location location, float radius) {
        Connection connection  = getConnection();
        String sql = "SELECT * FROM " + tableName + " WHERE ";
//        PreparedStatement ps = connection.prepareStatement();
    }
}