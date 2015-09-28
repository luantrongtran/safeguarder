package ifn701.safeguarder.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import ifn701.safeguarder.backend.entities.Accident;
import ifn701.safeguarder.backend.entities.User;

public class UserDao extends DAOBase {

    public static String tableName = "user";
    public static String colId = "id";
    public static String colFullName = "fullname";
    public static String colEmail = "email";
    public static String colPassword = "password";
    public static String colActivated = "activated";

    public User findById(int id) {
        Connection con = getConnection();
        String sql = "SELECT * FROM user WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = parseFromResultSet(rs);


                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insertANewUser(User user) {
        Connection con = getConnection();
        String sql = "INSERT INTO user (fullname) VALUES(?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getFullName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User parseFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(colId));
        user.setFullName(rs.getString(colFullName));
        user.setEmail(rs.getString(colEmail));
        user.setPassword(rs.getString(colPassword));
        user.setActivated(rs.getBoolean(colActivated));

        return user;
    }
}