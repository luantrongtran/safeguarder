package ifn701.safeguarder.webservices;

import android.os.AsyncTask;

import java.io.IOException;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.ResultCode;
import ifn701.safeguarder.backend.myApi.model.UserSetting;

/**
 * Created by lua on 26/09/2015.
 */
public class UpdateUserSettingService extends AsyncTask<UserSetting, Void, ResultCode> {

    IUpdateUserSettingService interfaceUpdate;
    public UpdateUserSettingService(IUpdateUserSettingService inter) {
        this.interfaceUpdate = inter;
    }

    @Override
    protected ResultCode doInBackground(UserSetting... params) {
        UserSetting userSetting = params[0];

        MyApi myApi = BackendApiProvider.getPatientApi();

        ResultCode resultCode = new ResultCode();
        try {
            resultCode = myApi.updateUserSettings(userSetting).execute();
        } catch (IOException e) {
            e.printStackTrace();
            resultCode.setResult(false);
        }

        return resultCode;
    }

    @Override
    protected void onPostExecute(ResultCode resultCode) {
        if(interfaceUpdate == null) {
            return;
        }
        interfaceUpdate.onUserSettingsUpdated(resultCode);
    }
}
