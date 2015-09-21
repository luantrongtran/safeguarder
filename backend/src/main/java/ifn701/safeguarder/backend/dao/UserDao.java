package ifn701.safeguarder.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import ifn701.safeguarder.backend.entities.User;

public class UserDao extends DAOBase {

    public static String tableName = "user";
    public static String colId = "id";
    public static String colFullName = "fullName";
    public static String colEmail = "email";
    public static String colPassword = "password";
    public static String colActivated = "activated";



//    /**
//     * Get all accidents in the area which has centroid is location variable
//     * and radius which is radius variable.
//     * @param lat the latitude of the centroid of the area
//     * @param lon the longtitude of the centroid of the area
//     * @param radius the radius from the centroid indicated by lat and lon
//     */
//    public Vector<Accident> getAccidentsInSelectedArea(double lat, double lon, float radius) {
//        Connection connection  = getConnection();
//        Vector<Accident> accidentVector = new Vector<Accident>();
//        String sql = "SELECT * FROM " + tableName + " " +
//                "WHERE get_distance_between_2_points_in_m(" + colLat + "," + colLon + ", ?, ?) < ?";

    public User loginUser(String email, String password) {
        Connection connection = getConnection();
//        Vector<User> userVector = new Vector<User>();
//        String sql = "SELECT * FROM " + tableName + " " +
//                "WHERE get_user_info(" + colFullName + "," + colEmail + "," + colPassword + "," + colActivated
//        ")";

        String sql = "SELECT * FROM "+ tableName + " " + "WHERE " + colEmail + " =? AND "+colPassword+" =?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            //ps.setString(1, fullName);
            ps.setString(1, email);
            ps.setString(2, password);
            //ps.setBoolean(4, activated);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = parseFromResultSet(rs);
                return user;
//                userVector.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public User findById(int id) {
//        Connection con = getConnection();
//        String sql = "SELECT * FROM patient WHERE id = ?";
//        PreparedStatement ps = null;
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, id);
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public void signUp(User user) {
        Connection con = getConnection();
        String sql = "INSERT INTO user (fullName,email,password,activated) VALUES(?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setBoolean(4, false);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User parseFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(colId));
        user.setFullName(rs.getString(colFullName));
        user.setEmail(rs.getString(colEmail));
        user.setPassword(rs.getString(colPassword));
        user.setActivated(rs.getBoolean(colActivated));

        return user;
    }
}