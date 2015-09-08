package ifn701.safegurader.backend.local_database_test_cases;

import org.junit.Before;
import org.junit.Test;

import ifn701.safeguarder.backend.dao.UserDao;
import ifn701.safeguarder.backend.entities.User;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class UserCRUDTest extends LocalDatabaseTest{

    UserDao userDao;
    UserDao spyUserDao;
    @Before
    public void setUp(){
        userDao = new UserDao();
        spyUserDao = spy(userDao);
        doReturn(getSpiedConnectionProvider()).when(spyUserDao).getConnection();
    }

    @Test
    public void testInsertANewPatient() {
        User mockPatient = mock(User.class);
        when(mockPatient.getFullName()).thenReturn("Trong Luan Tran");
        when(mockPatient.getId()).thenReturn(1);

        spyUserDao.insertANewUser(mockPatient);
    }
}