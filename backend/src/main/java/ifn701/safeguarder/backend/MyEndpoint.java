/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package ifn701.safeguarder.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.inject.Named;

import ifn701.safeguarder.backend.dao.UserDao;
import ifn701.safeguarder.backend.entities.ResultCode;

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
public class MyEndpoint {
    private static final Logger logger = Logger.getLogger(MyEndpoint.class.getName());

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    @ApiMethod(name="getConnectionError")
    public ResultCode getConnectionError() {
        String err = "no error";
        Connection connection = null;
        try {
            String url =
                    "jdbc:google:mysql://safeguarder-1097:safeguarder/safeguarder?user=root";
            Class.forName("com.mysql.jdbc.GoogleDriver");
            connection = DriverManager.getConnection(url);

            String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "j@ex.com");
            ps.setString(2, "abcd");
            ResultSet rs =ps.executeQuery();

            while(rs.next()) {
                err += "\n" + rs.getString("id");
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.info(e.toString());
            err = e.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info(e.toString());
            err= e.toString();

        }

        UserDao userDao = new UserDao();
        int userid = userDao.loginUser("j@ex.com", "abcd").getId();

        err += "\n userdao: " + userid;

        ResultCode rc = new ResultCode();
        rc.setResult(connection == null);
        rc.setMsg(err);
        return rc;
    }
}
