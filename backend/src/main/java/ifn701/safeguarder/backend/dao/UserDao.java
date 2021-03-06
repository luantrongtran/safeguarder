package ifn701.safeguarder.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ifn701.safeguarder.backend.Constants;
import ifn701.safeguarder.backend.entities.Accident;
import ifn701.safeguarder.backend.entities.User;
import ifn701.safeguarder.backend.entities.UserSetting;

public class UserDao extends DAOBase {

    public static String tableName = "user";
    public static String colId = "id";
    public static String colFullName = "fullname";
    public static String colEmail = "email";
    public static String colPassword = "password";
    public static String colActivated = "activated";
    public static String colToken = "token";
    public static String colProfilePicture = "profile_picture";
    public static String colAdmin = "is_admin";

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

     public User signUp(User user) {

         if(isEmailUsed(user.getEmail())){
             return null;
         }

         Connection con = getConnection();
         String sql = "INSERT INTO user (fullName,email,password,activated) VALUES(?,?,?,?)";

         PreparedStatement ps = null;
         try {
             ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             ps.setString(1, user.getFullName());
             ps.setString(2, user.getEmail());
             ps.setString(3, user.getPassword());
             ps.setBoolean(4, false);
             ps.executeUpdate();

             ResultSet rs = ps.getGeneratedKeys();
             while (rs.next()) {
                 //Create new settings for the new user
                 int id = rs.getInt(1);
                 UserSetting userSetting = new UserSetting();
                 userSetting.setUserId(id);
                 userSetting.setRadius(Constants.defaultUserRadius);//default radius is 100
                 UserSettingDao userSettingDao = new UserSettingDao();
                 userSettingDao.insertNewUserSetting(userSetting);
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return user;
     }

    public User parseFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(colId));
        user.setFullName(rs.getString(colFullName));
        user.setEmail(rs.getString(colEmail));
        user.setPassword(rs.getString(colPassword));
        user.setActivated(rs.getBoolean(colActivated));
        user.setProfilePictureUrl(rs.getString(colProfilePicture));
        user.setAdmin(rs.getBoolean(colAdmin));

        return user;
    }

    public boolean saveToken(int userId, String token) {
        String sql = "UPDATE " + tableName + " SET " + colToken + " = ? WHERE " + colId + " = ?";
        PreparedStatement ps = null;

        boolean b = false;
        try {
            ps = getConnection().prepareStatement(sql);
            ps.setString(1, token);
            ps.setInt(2, userId);

            int i = ps.executeUpdate();
            b = (i>0)?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * Update user's profile picture
     * @param url new url of the new image
     */
    public int updateProfilePicture(int userId, String url) {
        String sql = "UPDATE " + tableName + " SET " + colProfilePicture + " = ? WHERE " +
                colId + " = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, url);
            ps.setInt(2, userId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Checks if an emails is used for signing up or not
     * @param email
     * @return true if the email is already existing, otherwise, returns false
     */
    public boolean isEmailUsed(String email) {
        Connection con = getConnection();
        String sql = "SELECT * FROM user WHERE " + colEmail + " = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                //if could find a user then the email has been used
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * remove GCM token for a user
     * @param userId
     */
    public void removeGCMToken(int userId) {
        String sql = "UPDATE " + tableName + " SET " + colToken + "='' WHERE " + colId + " = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, userId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}