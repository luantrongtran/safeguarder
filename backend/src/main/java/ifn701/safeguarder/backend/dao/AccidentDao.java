package ifn701.safeguarder.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

import ifn701.safeguarder.backend.entities.Accident;
import ifn701.safeguarder.backend.entities.Location;

public class AccidentDao extends DAOBase {
	
	public static String tableName = "accident";
    public static String colId = "id";
    public static String colUserId = "user_id=";
    public static String colName = "name";
    public static String colType = "type";
    public static String colTime = "time";
    public static String colLat = "lat";
    public static String colLon = "lon";
    public static String colObservationLevel = "observation_level";
    public static String colDescription = "description";
    public static String colImage1 = "image1";
    public static String colImage2 = "image2";
    public static String colImage3 = "image3";


    /**
     * Get all accidents in the area which has centroid is location variable
     * and radius which is radius variable.
     * @param lat the latitude of the centroid of the area
     * @param lon the longtitude of the centroid of the area
     * @param radius the radius from the centroid indicated by lat and lon
     */
    public Vector<Accident> getAccidentsInSelectedArea(double lat, double lon, float radius) {
        Connection connection  = getConnection();
        Vector<Accident> accidentVector = new Vector<Accident>();
        String sql = "SELECT * FROM " + tableName + " " +
                "WHERE get_distance_between_2_points_in_km(" + colLat + "," + colLon + ", ?, ?) < ?";


        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, lat);
            ps.setDouble(2, lon);

            ps.setFloat(3, radius);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Accident accident = parseFromResultSet(rs);
                accidentVector.add(accident);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accidentVector;
    }

    public Accident findById(int id) {
        Connection con = getConnection();

        String sql = "SELECT * FROM accident WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insertANewAccident(Accident accident) {
        Connection con = getConnection();
        String sql = "INSERT INTO accident (name, type, time, lat, lon, observation_level, " +
                "description, image1, image2, image3, user_Id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getType());

            Timestamp time = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(3, time);
            ps.setDouble(4, accident.getLat());
            ps.setDouble(5, accident.getLon());
            ps.setInt(6, accident.getObservation_level());
            ps.setString(7, accident.getDescription());
            ps.setBlob(8, accident.getImage1());
            ps.setBlob(9, accident.getImage2());
            ps.setBlob(10, accident.getImage3());
            ps.setInt(11, accident.getUserId()); //REMEMBER

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Accident parseFromResultSet(ResultSet rs) throws SQLException {
        Accident accident = new Accident();
        accident.setId(rs.getInt(colId));
        accident.setUserId(rs.getInt(colUserId));
        accident.setName(rs.getString(colName));
        accident.setType(rs.getString(colType));
        accident.setTime(rs.getLong(colTime));
        accident.setLat(rs.getDouble(colLat));
        accident.setLon(rs.getDouble(colLon));
        accident.setObservation_level(rs.getInt(colObservationLevel));
        accident.setDescription(rs.getString(colDescription));
        accident.setImage1(rs.getBlob(colImage1));
        accident.setImage2(rs.getBlob(colImage2));
        accident.setImage3(rs.getBlob(colImage3));

        return accident;
    }
}