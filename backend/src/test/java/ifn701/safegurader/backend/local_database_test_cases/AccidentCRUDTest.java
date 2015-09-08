package ifn701.safegurader.backend.local_database_test_cases;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;
//import java.sql.Blob;
//import javax.sql.rowset.serial.SerialBlob;  ??

import ifn701.safeguarder.backend.dao.AccidentDao;
import ifn701.safeguarder.backend.entities.Accident;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class AccidentCRUDTest extends LocalDatabaseTest{
    AccidentDao accidentDao;
    AccidentDao spyAccidentDao;
    @Before
    public void setUp(){
        accidentDao = new AccidentDao();
        spyAccidentDao = spy(accidentDao);
        doReturn(getSpiedConnectionProvider()).when(spyAccidentDao).getConnection();
    }

    @Test
    public void testInsertANewAccident() {
        Accident mockAccident = mock(Accident.class);
        when(mockAccident.getUserId()).thenReturn(1);
        when(mockAccident.getName()).thenReturn("AccidentName");
        when(mockAccident.getType()).thenReturn("AccidentType");
        when(mockAccident.getTime()).thenReturn(new Timestamp(new Date().getTime()));
        when(mockAccident.getLat()).thenReturn(1.1f);
        when(mockAccident.getLon()).thenReturn(1.1f);
        when(mockAccident.getObservation_level()).thenReturn(1);
        when(mockAccident.getDescription()).thenReturn("Accident Description");
        //when(mockAccident.getImage1()).thenReturn(1));
        //when(mockAccident.getImage2()).thenReturn(1));
        //when(mockAccident.getImage3()).thenReturn(1));

        spyAccidentDao.insertANewAccident(mockAccident);
    }
}
