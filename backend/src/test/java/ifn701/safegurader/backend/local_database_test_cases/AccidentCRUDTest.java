package ifn701.safegurader.backend.local_database_test_cases;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import ifn701.safeguarder.backend.dao.AccidentDao;
import ifn701.safeguarder.backend.entities.Accident;
import ifn701.safeguarder.backend.entities.Location;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class AccidentCRUDTest extends LocalDatabaseTest {
    AccidentDao accidentDao;
    AccidentDao spyAccidentDao;
    @Before
    public void setUp(){
        accidentDao = new AccidentDao();
        spyAccidentDao = spy(accidentDao);
        doReturn(getSpiedConnectionProvider()).when(spyAccidentDao).getConnection();
    }

    @Test
    public void testInsertANewPatient() {
        Accident mockAccident = mock(Accident.class);
        when(mockAccident.getName()).thenReturn("Accident");
        when(mockAccident.getUserId()).thenReturn(1);

        spyAccidentDao.insertANewAccident(mockAccident);
    }

    /**
     * Test SQL procedure get_distance_between_2_points_in_km
     */
    @Test
    public void testIfAAccidentWithinARange() {
        /**
         * The expected distance from Garden Library to Art Museum is around 60.8m
         */
        float expectedDistance= 1f;//km
        float selectedRadius = 2;//km

        Connection con = getSpiedConnectionProvider();
        String sql = "SELECT get_distance_between_2_points_in_km(?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            //QUT library
            ps.setFloat(1, -27.477047f);
            ps.setFloat(2, 153.028405f);

            //QUT Museum Art
            ps.setFloat(3, -27.477164f);
            ps.setFloat(4, 153.027512f);

            ResultSet rs = ps.executeQuery();
            float distance = 2;//km
            while(rs.next()) {
                distance = rs.getFloat(1);
            }

            assertTrue("The distance between GP library to QUT Art Museum should be less than 1km",
                    (distance < expectedDistance));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test SQL procedure get_distance_between_2_points_in_km
     */
    @Test
    public void testIfAAccidentWithinARangeFailed() {
        /**
         * The expected distance from Garden Library to Art Museum is around 60.8m
         */
        float expectedDistance= 0.05f;//km
        float selectedRadius = 2;//km

        Connection con = getSpiedConnectionProvider();
        String sql = "SELECT get_distance_between_2_points_in_km(?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            //QUT library
            ps.setFloat(1, -27.477047f);
            ps.setFloat(2, 153.028405f);

            //QUT Museum Art
            ps.setFloat(3, -27.477164f);
            ps.setFloat(4, 153.027512f);

            ResultSet rs = ps.executeQuery();
            float distance = 1f;//km
            while(rs.next()) {
                distance = rs.getFloat(1);
            }

            assertFalse("The distance between GP library to QUT Art Museum should be more than 0.05km",
                    (distance < expectedDistance));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
