//package ifn701.safeguarder.backend.webservices;
//
///**
// * Created by Jeevan on 9/21/2015.
// */
//import com.google.api.server.spi.config.Api;
//import com.google.api.server.spi.config.ApiMethod;
//import com.google.api.server.spi.config.ApiNamespace;
//import java.util.logging.Logger;
//import javax.inject.Named;
//
//
//import ifn701.safeguarder.backend.dao.UserDao;
//import ifn701.safeguarder.backend.entities.User;
////import ifn701.safeguarder.backend.entities.UserList;
//
///**
// * An endpoint class we are exposing
// */
//@Api(
//        name = "myApi",
//        version = "v1",
//        namespace = @ApiNamespace(
//                ownerDomain = "backend.safeguarder.ifn701",
//                ownerName = "backend.safeguarder.ifn701",
//                packagePath = ""
//        )
//)
//public class Signup {
//    private static final Logger logger = Logger.getLogger(Login.class.getName());
//
//    /**
//     * @param user the fullname of the user
//     * @return all the users that are registered
//     */
//    @ApiMethod(name = "signup")
//    public void signup(User user) {
//        UserDao userDao = new UserDao();
//        return userDao.signUp(user);
//
////        Vector<User> userVector = userDao.getUserInfo(fullName,email,password,activated);
////        UserList userList = new UserList();
////        userList.setUserList(userVector);
////        return userList;
//
//    }
//
//    @ApiMethod(name = "getUser")
//    public User getUser(@Named("id") Long id){
//        logger.info("Caling getUser method");
//        return null;
//    }
//
//    @ApiMethod(name = "insertUser")
//    public User insertUser(User user){
//        UserDao userdao = new UserDao();
//        userdao.signUp(user);
//        return null;
//    }
//
//}