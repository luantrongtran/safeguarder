package ifn701.safeguarder.webservices.google_cloud_service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.ResultCode;

/**
 * Created by lua on 1/10/2015.
 */
public class RegisterTokenToSafeguarderBackend extends AsyncTask<String, Void, ResultCode> {
    Context context;

    public RegisterTokenToSafeguarderBackend(Context context) {
        this.context = context;
    }

    @Override
    protected ResultCode doInBackground(String... params) {
        MyApi myApi = BackendApiProvider.getPatientApi();

        String token = params[0];
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);

        ResultCode rs = null;
        try {
            rs = myApi.saveToken(userInfoPreferences.getUserId(), token).execute();

            return rs;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(rs == null || rs.getResult() == false) {
            Log.e(Constants.APPLICATION_ID, "Cannot save token");
        }
        return rs;
    }
}
