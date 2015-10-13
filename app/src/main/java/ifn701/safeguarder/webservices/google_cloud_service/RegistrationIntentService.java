package ifn701.safeguarder.webservices.google_cloud_service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import ifn701.safeguarder.BackendApiProvider;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.GCMSharedPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.MyApi;
import ifn701.safeguarder.backend.myApi.model.ResultCode;

public class RegistrationIntentService extends IntentService {

    public static String TAG = "RegistrationIntentService";
    public RegistrationIntentService() {
        super(TAG);
    }

    public static final String NEW_ACCIDENT_EVENT = "newaccident";

    public static final String[] TOPICS = {NEW_ACCIDENT_EVENT};

    @Override
    protected void onHandleIntent(Intent intent) {
        GCMSharedPreferences gcmSharedPreferences = new GCMSharedPreferences(this);
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM token: " + token);

            registerToken(token);
            subscribeTopics(token);
            gcmSharedPreferences.setIsTokenSentToServer(true);
        } catch (IOException e) {
            e.printStackTrace();
            gcmSharedPreferences.setIsTokenSentToServer(false);
        }
    }

    private void registerToken(String token) {
        MyApi myApi = BackendApiProvider.getPatientApi();

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
        if(token == null || userInfoPreferences.getUserId()
                == Constants.sharedPreferences_integer_default_value) {
            Log.e(Constants.APPLICATION_ID, "Cannot save token to the Safeguarder backend: the token is null");
            return;
        }


        ResultCode rs = null;
        try {
            rs = myApi.saveToken(token, userInfoPreferences.getUserId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        if(rs == null || rs.getResult() == false) {
            Log.e(Constants.APPLICATION_ID, "Cannot save token to the Safeguarder backend");
        }
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            Log.i(TAG, "subscribe:" + topic);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}