package ifn701.safeguarder.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

import ifn701.safeguarder.backend.dao.UserDao;
import ifn701.safeguarder.backend.dao.UserSettingDao;
import ifn701.safeguarder.backend.entities.ResultCode;
import ifn701.safeguarder.backend.entities.UserSetting;

/**
 * Created by lua on 26/09/2015.
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
public class UserSettingsEndpoint {
    @ApiMethod(name = "updateUserSettings")
    public ResultCode updateUserSettings(UserSetting userSettings) {
        UserSettingDao userSettingDao = new UserSettingDao();
        boolean b = userSettingDao.updateUserSettings(userSettings);
        ResultCode resultCode = new ResultCode();
        resultCode.setResult(b);
        return resultCode;
    }

    @ApiMethod(name = "getUserSettingsByUserId")
    public UserSetting getUserSettingByUserId(@Named("userId") int userId) {
        UserSettingDao userSettingDao = new UserSettingDao();
        UserSetting userSetting = userSettingDao.getUserSettingsByUserId(userId);

        return userSetting;
    }
}
