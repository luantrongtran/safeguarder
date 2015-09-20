package ifn701.safeguarder.backgroundservices;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences
 */
public class LocationTrackerService extends IntentService {
    public static String ACTION = LocationTrackerService.class.getCanonicalName();

    public LocationTrackerService(){
        super("UpdateLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent in = new Intent(ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }
}