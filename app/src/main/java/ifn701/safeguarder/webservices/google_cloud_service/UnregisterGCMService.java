package ifn701.safeguarder.webservices.google_cloud_service;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.backend.myApi.MyApi;

/**
 * Created by lua on 27/10/2015.
 */
public class UnregisterGCMService extends AsyncTask <Void, Void, Void> {

    Context context;
    public UnregisterGCMService(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
        int userId = userInfoPreferences.getUserId();

        MyApi myApi = BackendApiProvider.getPatientApi();

        try {
            myApi.removeGCMToken(userId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
