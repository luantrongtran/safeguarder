package ifn701.safeguarder.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.inject.Named;

import ifn701.safeguarder.backend.dao.UserDao;
import ifn701.safeguarder.backend.entities.ResultCode;

/**
 * Created by lua on 3/09/2015.
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
public class UserEndpoint {
    @ApiMethod(name = "updateProfilePictureUrl")
    public ResultCode uploadProfilePicture(@Named("userId") int userId, @Named("imageUrl") String imageUrl) {
        try {
            imageUrl = URLDecoder.decode(imageUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UserDao userDao = new UserDao();
        int result = userDao.updateProfilePicture(userId, imageUrl);

        ResultCode rc = new ResultCode();
        if (result > 0) {
            //Updated successfully
            rc.setResult(true);
        } else {
            rc.setResult(false);
        }

        return rc;
    }
}
