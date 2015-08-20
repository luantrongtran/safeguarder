package ifn701.safegurader.backend.local_database_test_cases;

import org.junit.Before;
import org.junit.Test;

import ifn701.safeguarder.backend.dao.AccidentDao;
import ifn701.safeguarder.backend.dao.UserDao;
import ifn701.safeguarder.backend.entities.Accident;
import ifn701.safeguarder.backend.entities.User;

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
    public void testInsertANewPatient() {
        Accident mockAccident = mock(Accident.class);
        when(mockAccident.getName()).thenReturn("Accident");
        when(mockAccident.getUserId()).thenReturn(1);

        spyAccidentDao.insertANewAccident(mockAccident);
    }
}
