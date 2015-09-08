package ifn701.safeguarder.backend.dao;

import java.sql.DriverManager;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ifn701.safeguarder.backend.entities.Accident;

public class AccidentDao extends DAOBase {

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
        String sql = "INSERT INTO accident (user_Id, name, type, time, lat, lon, observation_level, " +
                "description, image1, image2, image3) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, accident.getUserId());
            ps.setString(2, accident.getName());
            ps.setString(3, accident.getType());

            Timestamp ts = new Timestamp(accident.getTime());
            ps.setTimestamp(4, ts);
            ps.setFloat(5, accident.getLat());
            ps.setFloat(6, accident.getLon());
            ps.setInt(7, accident.getObservation_level());
            ps.setString(8, accident.getDescription());
            ps.setBlob(9, accident.getImage1());
            ps.setBlob(10, accident.getImage2());
            ps.setBlob(11, accident.getImage3());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
