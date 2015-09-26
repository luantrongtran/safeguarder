package ifn701.safeguarder.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ifn701.safeguarder.backend.entities.User;
import ifn701.safeguarder.backend.entities.UserSetting;

/**
 * Created by lua on 26/09/2015.
 */
public class UserSettingDao extends DAOBase {

    public static String tableName = "user_setting";
    public static String colUserId = "user_id";
    public static String colHomeLocationLat = "home_location_lat";
    public static String colHomeLocationLon = "home_location_lon";
    public static String colHomeLocationAddress = "home_address";
    public static String colRadius = "radius";

    public UserSetting getUserSettingsByUserId(int userId) {

        String sql = "SELECT * FROM " + tableName + " WHERE " + colUserId + " = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                userId = rs.getInt(colUserId);
                double lat = rs.getDouble(colHomeLocationLat);
                double lon = rs.getDouble(colHomeLocationLon);
                float radius = rs.getFloat(colRadius);
                String address = rs.getString(colHomeLocationAddress);

                UserSetting userSetting = new UserSetting();
                userSetting.setHomeLocationLat(lat);
                userSetting.setHomeLocationLon(lon);
                userSetting.setRadius(radius);
                userSetting.setHomeAddress(address);
                userSetting.setUserId(userId);

                return userSetting;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insertNewUserSetting(UserSetting userSetting) {
        String sql = "INSERT INTO " + tableName + " (" + colHomeLocationAddress + ", "
                + colHomeLocationLat + ", " + colHomeLocationLon + ", " + colRadius
                + ", " + colUserId + ") VALUES(?,?,?,?,?)";

        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, userSetting.getHomeAddress());
            ps.setDouble(2, userSetting.getHomeLocationLat());
            ps.setDouble(3, userSetting.getHomeLocationLon());
            ps.setFloat(4, userSetting.getRadius());
            ps.setInt(5, userSetting.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserSettingByUserId(int userId) {
        String sql = "DELETE FROM " + tableName + " WHERE " + colUserId + " = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, userId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserSettings(UserSetting userSetting) {
        deleteUserSettingByUserId(userSetting.getUserId());
        insertNewUserSetting(userSetting);
    }
}
