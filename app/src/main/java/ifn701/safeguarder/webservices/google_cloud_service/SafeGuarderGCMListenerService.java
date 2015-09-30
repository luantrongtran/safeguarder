package ifn701.safeguarder.webservices.google_cloud_service;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by lua on 24/09/2015.
 */
public class SafeGuarderGCMListenerService extends GcmListenerService {
    public static String TAG = "SafeGuarderGCMListenerService";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
    }
}