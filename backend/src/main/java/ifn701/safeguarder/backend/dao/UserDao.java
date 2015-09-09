package ifn701.safeguarder.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ifn701.safeguarder.backend.entities.User;

public class UserDao extends DAOBase {

    public User findById(int id) {
        Connection con = getConnection();
        String sql = "SELECT * FROM patient WHERE id = ?";
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
}