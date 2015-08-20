package ifn701.safeguarder.backend.dao;

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
        String sql = "INSERT INTO accident (name, type, time, lat, lon, observation_level, " +
                "description, image1, image2, image3, user_Id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getType());
            ps.setTimestamp(3, accident.getTime());
            ps.setFloat(4, accident.getLat());
            ps.setFloat(5, accident.getLon());
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
}
