package ifn701.safeguarder.backend.webservices;

/**
 * Created by Jeevan on 9/21/2015.
 */
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.inject.Named;


import ifn701.safeguarder.backend.dao.UserDao;
import ifn701.safeguarder.backend.dao.UserSettingDao;
import ifn701.safeguarder.backend.entities.ResultCode;
import ifn701.safeguarder.backend.entities.User;
import ifn701.safeguarder.backend.entities.UserSetting;
//import ifn701.safeguarder.backend.entities.UserList;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.safeguarder.ifn701",
                ownerName = "backend.safeguarder.ifn701",
                packagePath = ""
        )
)
public class Login {
    private static final Logger logger = Logger.getLogger(Login.class.getName());

    /**
     * @param email    the email of the user
     * @param password the password of the user
     * @return all the users that are registered
     */
    @ApiMethod(name = "login")
    public User login( @Named("email") String email, @Named("password") String password) {
        UserDao userDao = new UserDao();
        User user = userDao.loginUser(email,password);

        UserSettingDao userSettingDao = new UserSettingDao();
        UserSetting userSetting = userSettingDao.getUserSettingsByUserId(user.getId());

        if(userSetting == null) {
            return null;
        }

        user.setUserSetting(userSetting);

        return user;

//        Vector<User> userVector = userDao.getUserInfo(fullName,email,password,activated);
//        UserList userList = new UserList();
//        userList.setUserList(userVector);
//        return userList;

    }

    @ApiMethod(name = "getUser")
    public User getUser(@Named("id") Long id){
        logger.info("Caling getUser method");
        return null;
    }

    @ApiMethod(name = "insertUser")
    public User insertUser(User user){
        UserDao userdao = new UserDao();
        userdao.signUp(user);
        return null;
    }

    @ApiMethod(name="saveToken")
    public ResultCode saveToken(@Named("userId")int userId, @Named("token") String token){
        UserDao userDao = new UserDao();
        boolean b = userDao.saveToken(userId, token);
        ResultCode rc = new ResultCode();
        rc.setResult(b);

        return rc;
    }
}