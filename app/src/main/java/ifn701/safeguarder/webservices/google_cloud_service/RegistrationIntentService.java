package ifn701.safeguarder.webservices.google_cloud_service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.GCMSharedPreferences;
import ifn701.safeguarder.R;

public class RegistrationIntentService extends IntentService {

    public static String TAG = "RegistrationIntentService";
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GCMSharedPreferences gcmSharedPreferences = new GCMSharedPreferences(this);
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM token: " + token);

            registerToken(token);
            gcmSharedPreferences.setIsTokenSentToServer(true);
        } catch (IOException e) {
            e.printStackTrace();
            gcmSharedPreferences.setIsTokenSentToServer(false);
        }
    }

    private void registerToken(String token) {
        RegisterTokenToSafeguarderBackend registerTokenToSafeguarderBackend
                = new RegisterTokenToSafeguarderBackend(this);
        registerTokenToSafeguarderBackend.execute(token);
    }
}